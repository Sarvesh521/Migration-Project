import json
import pymysql
from etl_generic.bson_reader import read_bson_file, normalize_bson_val
from etl_generic.schema_analyzer import is_document, is_array


def create_query_data(doc, schema, max_depth=2):
    """
    Extracts row dictionaries for the main table and all child tables
    from a single MongoDB document according to the inferred schema.
    Returns dict: { table_name: [list of row dicts] }
    """
    collection_name = schema["collection_name"]
    tables = schema["tables"]
    rows_by_table = {tname: [] for tname in tables.keys()}

    doc_id = str(doc.get("_id"))

    def process_doc(curr_doc, tbl_name, current_depth, parent_id):
        if not isinstance(curr_doc, dict):
            return

        tbl_info = tables[tbl_name]
        row = {}

        if tbl_info["is_child"]:
            row[tbl_info["parent_fk_col"]] = parent_id

        for key, raw_val in curr_doc.items():
            if key == "_id" and not tbl_info["is_child"]:
                row["_id"] = doc_id
                continue

            val = normalize_bson_val(raw_val)

            if is_document(val):
                if current_depth < max_depth:
                    child_tbl_name = f"{tbl_name}_{key}"
                    if child_tbl_name in tables:
                        process_doc(val, child_tbl_name, current_depth + 1, doc_id)
                else:
                    if key in tbl_info["columns"]:
                        row[key] = json.dumps(val) if val is not None else None
            elif is_array(val):
                if current_depth < max_depth:
                    child_tbl_name = f"{tbl_name}_{key}"
                    if child_tbl_name in tables:
                        child_tbl_info = tables[child_tbl_name]
                        for idx, item in enumerate(val):
                            child_row = {
                                child_tbl_info["parent_fk_col"]: doc_id,
                                "item_index": idx,
                            }
                            if is_document(item):
                                for k, v in item.items():
                                    if k in child_tbl_info["columns"]:
                                        child_row[k] = (
                                            json.dumps(v)
                                            if isinstance(v, (dict, list))
                                            else v
                                        )
                                rows_by_table[child_tbl_name].append(child_row)
                            else:
                                child_row["item_value"] = (
                                    json.dumps(item)
                                    if isinstance(item, (dict, list))
                                    else item
                                )
                                rows_by_table[child_tbl_name].append(child_row)
                else:
                    if key in tbl_info["columns"]:
                        row[key] = json.dumps(val) if val is not None else None
            else:
                if key in tbl_info["columns"]:
                    row[key] = val

        rows_by_table[tbl_name].append(row)

    process_doc(doc, collection_name, current_depth=1, parent_id=doc_id)
    return rows_by_table


def execute_batch_insert(cursor, table_name, table_schema, rows):
    if not rows:
        return

    cols = [
        c
        for c in table_schema["columns"].keys()
        if c != "id" or not table_schema["is_child"]
    ]

    placeholders = ", ".join(["%s"] * len(cols))
    quoted_cols = ", ".join([f"`{c}`" for c in cols])
    query = f"INSERT IGNORE INTO `{table_name}` ({quoted_cols}) VALUES ({placeholders})"

    params_list = []
    for r in rows:
        row_params = []
        for c in cols:
            val = r.get(c)
            row_params.append(val)
        params_list.append(row_params)

    cursor.executemany(query, params_list)


def run_worker_process(filepath, db_params, schema, start, limit, batch_size=500):
    """
    Algorithm 3: Transformation and Loading worker execution.
    Reads document batch, invokes create_query_data, executes batch inserts into MySQL.
    """
    conn = pymysql.connect(
        host=db_params["host"],
        port=db_params["port"],
        user=db_params["user"],
        password=db_params["password"],
        db=db_params["db"],
        autocommit=False,
        charset="utf8mb4",
    )

    try:
        tables = schema["tables"]
        main_table = schema["collection_name"]
        ordered_tables = [main_table] + [t for t in tables.keys() if t != main_table]

        doc_generator = read_bson_file(filepath, start=start, limit=limit)
        batch = []

        for doc in doc_generator:
            batch.append(doc)
            if len(batch) >= batch_size:
                _process_and_insert_batch(conn, batch, schema, ordered_tables)
                batch = []

        if batch:
            _process_and_insert_batch(conn, batch, schema, ordered_tables)

        conn.commit()
    except Exception as e:
        conn.rollback()
        raise e
    finally:
        conn.close()


def _process_and_insert_batch(conn, batch, schema, ordered_tables):
    tables = schema["tables"]
    batch_rows_by_table = {tname: [] for tname in tables.keys()}

    for doc in batch:
        doc_rows = create_query_data(doc, schema)
        for tname, rows in doc_rows.items():
            batch_rows_by_table[tname].extend(rows)

    max_retries = 5
    for attempt in range(max_retries):
        try:
            with conn.cursor() as cursor:
                for tname in ordered_tables:
                    rows = batch_rows_by_table[tname]
                    execute_batch_insert(cursor, tname, tables[tname], rows)
            conn.commit()
            break
        except pymysql.err.OperationalError as e:
            conn.rollback()
            if e.args[0] == 1213 and attempt < max_retries - 1:
                import time, random
                time.sleep(0.05 * (2 ** attempt) + random.uniform(0.01, 0.05))
            else:
                raise e
