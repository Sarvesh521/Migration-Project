import datetime
import bson
from bson.objectid import ObjectId
from bson.decimal128 import Decimal128
from etl_generic.bson_reader import read_bson_file


TYPE_HIERARCHY = {
    "NULL": 0,
    "BOOLEAN": 1,
    "INT": 2,
    "BIGINT": 3,
    "DOUBLE": 4,
    "DECIMAL": 5,
    "DATETIME": 6,
    "VARCHAR": 7,
    "TEXT": 8,
    "LONGTEXT": 9,
    "JSON": 10,
}


def is_document(val):
    return isinstance(val, dict)


def is_array(val):
    return isinstance(val, list)


def detect_scalar_type(val):
    """
    Detects the scalar data type of a value.
    """
    if val is None:
        return "NULL", 0
    if isinstance(val, bool):
        return "BOOLEAN", 0
    if isinstance(val, int):
        if -2147483648 <= val <= 2147483647:
            return "INT", 0
        else:
            return "BIGINT", 0
    if isinstance(val, float):
        return "DOUBLE", 0
    if isinstance(val, (Decimal128, str)) and type(val).__name__ == "Decimal128":
        return "DECIMAL", 0
    if isinstance(val, datetime.datetime):
        return "DATETIME", 0
    if isinstance(val, ObjectId):
        return "VARCHAR", 24

    # String or string fallback
    sval = str(val)
    slen = len(sval)
    if slen <= 255:
        return "VARCHAR", slen
    elif slen <= 65535:
        return "TEXT", slen
    else:
        return "LONGTEXT", slen


def widen_type(current_type, current_max_len, new_type, new_max_len):
    """
    Widens column types according to the type hierarchy.
    """
    if current_type == "NULL":
        return new_type, new_max_len
    if new_type == "NULL":
        return current_type, current_max_len

    if current_type == new_type:
        return current_type, max(current_max_len, new_max_len)

    c_rank = TYPE_HIERARCHY.get(current_type, 7)
    n_rank = TYPE_HIERARCHY.get(new_type, 7)

    # Numeric widening: INT + BIGINT -> BIGINT; INT/BIGINT + DOUBLE -> DOUBLE
    if current_type in ("INT", "BIGINT") and new_type in ("INT", "BIGINT"):
        return "BIGINT", 0
    if current_type in ("INT", "BIGINT", "DOUBLE") and new_type in (
        "INT",
        "BIGINT",
        "DOUBLE",
    ):
        return "DOUBLE", 0

    # Higher rank wins
    if n_rank > c_rank:
        target_type = new_type
    else:
        target_type = current_type

    target_len = max(current_max_len, new_max_len)
    return target_type, target_len


def get_mysql_data_type(type_name, max_len=0, num_cols_in_table=0):
    """
    Maps abstract type name to MySQL SQL datatype definition.
    For wide tables (>30 columns), uses TEXT for string/null fields to prevent MySQL InnoDB row size limit (>8126 bytes).
    """
    if type_name == "BOOLEAN":
        return "TINYINT(1)"
    elif type_name == "INT":
        return "INT"
    elif type_name == "BIGINT":
        return "BIGINT"
    elif type_name == "DOUBLE":
        return "DOUBLE"
    elif type_name == "DECIMAL":
        return "DECIMAL(38, 10)"
    elif type_name == "DATETIME":
        return "DATETIME(6)"
    elif type_name == "NULL":
        if num_cols_in_table > 30:
            return "TEXT"
        return "VARCHAR(64)"
    elif type_name == "VARCHAR":
        if num_cols_in_table > 30:
            return "TEXT"
        length = max(max_len, 64)
        if length > 255:
            return "TEXT"
        return f"VARCHAR({length})"
    elif type_name in ("TEXT", "LONGTEXT", "JSON"):
        return type_name
    return "TEXT" if num_cols_in_table > 30 else "VARCHAR(255)"


class SchemaAnalyzer:
    """
    Algorithm 1: Schema Analyzer Strategy.
    Scans collection documents to dynamically build table and column schemas
    for main document and nested documents/arrays up to depth level k.
    """

    def __init__(self, collection_name, max_depth=2):
        self.collection_name = collection_name
        self.max_depth = max_depth
        # Dict of table_name -> table schema info
        self.tables = {}
        self._init_table(
            table_name=collection_name,
            is_child=False,
            parent_table=None,
            parent_fk_col=None,
            primary_key="_id",
        )

    def _init_table(
        self, table_name, is_child, parent_table, parent_fk_col, primary_key
    ):
        if table_name not in self.tables:
            self.tables[table_name] = {
                "table_name": table_name,
                "is_child": is_child,
                "parent_table": parent_table,
                "parent_fk_col": parent_fk_col,
                "primary_key": primary_key,
                "columns": {},
                "record_count": 0,
            }
            if is_child:
                # Child tables have auto increment integer primary key 'id'
                self.tables[table_name]["columns"]["id"] = {
                    "type": "INT",
                    "sql_type": "INT AUTO_INCREMENT PRIMARY KEY",
                    "nullable": False,
                    "max_len": 0,
                }
                # And foreign key pointing to parent
                self.tables[table_name]["columns"][parent_fk_col] = {
                    "type": "VARCHAR",
                    "sql_type": "VARCHAR(64)",
                    "nullable": False,
                    "max_len": 64,
                }
            else:
                # Main table has _id as primary key
                self.tables[table_name]["columns"]["_id"] = {
                    "type": "VARCHAR",
                    "sql_type": "VARCHAR(64) PRIMARY KEY",
                    "nullable": False,
                    "max_len": 64,
                }

    def analyze_document(self, doc, table_name=None, current_depth=1, parent_id=None):
        if table_name is None:
            table_name = self.collection_name

        tbl = self.tables[table_name]
        tbl["record_count"] += 1

        for col_name, val in doc.items():
            if col_name == "_id" and not tbl["is_child"]:
                # Primary key _id already initialized
                continue

            if is_document(val):
                if current_depth < self.max_depth:
                    child_table_name = f"{table_name}_{col_name}"
                    parent_fk_col = f"{table_name}_id"
                    self._init_table(
                        table_name=child_table_name,
                        is_child=True,
                        parent_table=table_name,
                        parent_fk_col=parent_fk_col,
                        primary_key="id",
                    )
                    self.analyze_document(
                        doc=val,
                        table_name=child_table_name,
                        current_depth=current_depth + 1,
                        parent_id=parent_id,
                    )
                else:
                    # Beyond max depth: store as JSON string
                    self._add_column_value(tbl, col_name, "JSON", 0)
            elif is_array(val):
                if current_depth < self.max_depth:
                    child_table_name = f"{table_name}_{col_name}"
                    parent_fk_col = f"{table_name}_id"
                    self._init_table(
                        table_name=child_table_name,
                        is_child=True,
                        parent_table=table_name,
                        parent_fk_col=parent_fk_col,
                        primary_key="id",
                    )
                    child_tbl = self.tables[child_table_name]
                    # Add item_index column
                    if "item_index" not in child_tbl["columns"]:
                        child_tbl["columns"]["item_index"] = {
                            "type": "INT",
                            "sql_type": "INT",
                            "nullable": False,
                            "max_len": 0,
                        }

                    for idx, item in enumerate(val):
                        if is_document(item):
                            self.analyze_document(
                                doc=item,
                                table_name=child_table_name,
                                current_depth=current_depth + 1,
                                parent_id=parent_id,
                            )
                        else:
                            # Primitive array item
                            stype, slen = detect_scalar_type(item)
                            self._add_column_value(
                                child_tbl, "item_value", stype, slen
                            )
                else:
                    self._add_column_value(tbl, col_name, "JSON", 0)
            else:
                stype, slen = detect_scalar_type(val)
                self._add_column_value(tbl, col_name, stype, slen)

    def _add_column_value(self, tbl, col_name, stype, slen):
        cols = tbl["columns"]
        if col_name not in cols:
            cols[col_name] = {
                "type": stype,
                "max_len": slen,
                "nullable": True,
                "sql_type": get_mysql_data_type(stype, slen, len(cols) + 1),
            }
        else:
            curr = cols[col_name]
            wtype, wlen = widen_type(curr["type"], curr["max_len"], stype, slen)
            curr["type"] = wtype
            curr["max_len"] = wlen
            curr["sql_type"] = get_mysql_data_type(wtype, wlen, len(cols))

    def analyze_bson_file(self, filepath):
        for doc in read_bson_file(filepath):
            self.analyze_document(doc)
        return self.get_schema()

    def get_schema(self):
        # Finalize sql_type for all columns considering total column count
        for tbl_name, tbl in self.tables.items():
            num_cols = len(tbl["columns"])
            for col_name, cinfo in tbl["columns"].items():
                if col_name == "id" and tbl["is_child"]:
                    continue
                if col_name == tbl.get("parent_fk_col"):
                    continue
                if col_name == "_id" and not tbl["is_child"]:
                    continue
                cinfo["sql_type"] = get_mysql_data_type(
                    cinfo["type"], cinfo.get("max_len", 0), num_cols
                )
        return {"collection_name": self.collection_name, "tables": self.tables}
