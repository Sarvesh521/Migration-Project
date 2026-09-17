import math
from multiprocessing import Process
import pymysql
from urllib.parse import urlparse
from etl_generic.bson_reader import count_bson_file
from etl_generic.transformer_loader import run_worker_process


def parse_db_uri(db_uri):
    """
    Parses a MySQL DB URI like mysql+pymysql://user:pass@host:port/dbname or mysql://...
    """
    cleaned_uri = db_uri
    if cleaned_uri.startswith("mysql+pymysql://"):
        cleaned_uri = "mysql://" + cleaned_uri[len("mysql+pymysql://") :]

    parsed = urlparse(cleaned_uri)
    return {
        "host": parsed.hostname or "127.0.0.1",
        "port": parsed.port or 3306,
        "user": parsed.username or "root",
        "password": parsed.password or "",
        "db": parsed.path.lstrip("/"),
    }


def get_db_connection(db_params):
    return pymysql.connect(
        host=db_params["host"],
        port=db_params["port"],
        user=db_params["user"],
        password=db_params["password"],
        db=db_params["db"],
        autocommit=True,
        charset="utf8mb4",
    )


class ProcessInitiator:
    """
    Algorithm 2: Processes Initiation strategy from Aftab et al. (2020).
    Responsible for initializing schema in MySQL destination database,
    computing logical partitions, and spawning concurrent ETL worker processes.
    """

    def __init__(self, db_uri, num_workers=4, batch_size=500):
        self.db_uri = db_uri
        self.db_params = parse_db_uri(db_uri)
        self.num_workers = num_workers
        self.batch_size = batch_size

    def initialize_schema(self, schema, recreate=False):
        """
        Creates target MySQL tables for all collections and child tables based on approved schema.
        """
        conn = get_db_connection(self.db_params)
        try:
            with conn.cursor() as cursor:
                try:
                    cursor.execute("SET SESSION innodb_strict_mode=0;")
                except Exception:
                    pass
                try:
                    cursor.execute("SET FOREIGN_KEY_CHECKS=0;")
                except Exception:
                    pass

                tables = schema["tables"]
                # Order tables: root tables first, then child tables
                root_keys = [t for t, info in tables.items() if not info.get("is_child")]
                child_keys = [t for t, info in tables.items() if info.get("is_child")]
                ordered_tbl_keys = root_keys + child_keys

                if recreate:
                    # Drop tables in reverse order
                    for tbl_key in reversed(ordered_tbl_keys):
                        tbl_info = tables[tbl_key]
                        if tbl_info.get("excluded"):
                            continue
                        actual_name = tbl_info.get("table_name", tbl_key)
                        cursor.execute(f"DROP TABLE IF EXISTS `{actual_name}`")

                # Pass 1: Create all tables without inline FKs first
                for tbl_key in ordered_tbl_keys:
                    tbl_info = tables[tbl_key]
                    if tbl_info.get("excluded"):
                        continue

                    actual_table_name = tbl_info.get("table_name", tbl_key)
                    col_defs = []

                    # Find all FK columns for this table to ensure type compatibility
                    fk_col_map = {}
                    user_fks = tbl_info.get("fk_constraints", [])
                    if user_fks:
                        for fk in user_fks:
                            fk_col_map[fk.get("fk_col")] = fk
                    elif tbl_info.get("is_child"):
                        fk_col_map[tbl_info["parent_fk_col"]] = True

                    for col_key, cinfo in tbl_info["columns"].items():
                        if cinfo.get("excluded"):
                            continue
                        col_name = cinfo.get("renamed_to", col_key)
                        sql_type = cinfo["sql_type"]

                        # If this column is a Foreign Key and type is TEXT/LONGTEXT, enforce VARCHAR(64) for MySQL FK compatibility
                        if col_key in fk_col_map or col_name in fk_col_map:
                            if "TEXT" in sql_type or "LONGTEXT" in sql_type:
                                sql_type = "VARCHAR(64)"

                        col_defs.append(f"`{col_name}` {sql_type}")

                    if not col_defs:
                        continue

                    create_query = f"CREATE TABLE IF NOT EXISTS `{actual_table_name}` ({', '.join(col_defs)}) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4;"
                    cursor.execute(create_query)

                # Pass 2: Add all Foreign Key constraints using ALTER TABLE
                for tbl_key in ordered_tbl_keys:
                    tbl_info = tables[tbl_key]
                    if tbl_info.get("excluded"):
                        continue

                    actual_table_name = tbl_info.get("table_name", tbl_key)
                    user_fks = tbl_info.get("fk_constraints", [])

                    fks_to_apply = []
                    if user_fks:
                        for fk in user_fks:
                            fks_to_apply.append(fk)
                    elif tbl_info.get("is_child"):
                        parent_tbl_key = tbl_info.get("parent_table")
                        if parent_tbl_key:
                            parent_pk = tables[parent_tbl_key]["primary_key"] if parent_tbl_key in tables else "_id"
                            fks_to_apply.append({
                                "fk_col": tbl_info["parent_fk_col"],
                                "ref_table": parent_tbl_key,
                                "ref_col": parent_pk,
                                "on_delete": "CASCADE"
                            })

                    for fk_idx, fk in enumerate(fks_to_apply):
                        ref_tbl_key = fk.get("ref_table")
                        if ref_tbl_key in tables and not tables[ref_tbl_key].get("excluded"):
                            ref_actual_name = tables[ref_tbl_key].get("table_name", ref_tbl_key)
                            fk_col = fk.get("fk_col")
                            ref_col = fk.get("ref_col") or tables[ref_tbl_key].get("primary_key", "_id")
                            on_del = fk.get("on_delete", "CASCADE").upper()
                            fk_name = f"fk_{actual_table_name}_{fk_col}_{fk_idx}"

                            alter_query = f"ALTER TABLE `{actual_table_name}` ADD CONSTRAINT `{fk_name}` FOREIGN KEY (`{fk_col}`) REFERENCES `{ref_actual_name}`(`{ref_col}`) ON DELETE {on_del};"
                            try:
                                cursor.execute(alter_query)
                            except Exception as fk_err:
                                print(f"Warning: Could not add FK on `{actual_table_name}`.`{fk_col}` referencing `{ref_actual_name}`.`{ref_col}`: {fk_err}")

                try:
                    cursor.execute("SET FOREIGN_KEY_CHECKS=1;")
                except Exception:
                    pass
        finally:
            conn.close()

    def start_etl_for_file(self, filepath, collection_name, schema):
        """
        Initiates multi-process ETL for a single BSON file.
        """
        length = count_bson_file(filepath)
        if length == 0:
            print(f"Skipping empty file {filepath}")
            return

        # Determine worker process partitioning
        n = min(self.num_workers, length)
        if n <= 1:
            # Run in single process
            run_worker_process(
                filepath=filepath,
                db_params=self.db_params,
                schema=schema,
                start=0,
                limit=length,
                batch_size=self.batch_size,
            )
            return

        limit_per_process = math.ceil(length / n)
        processes = []

        for j in range(n):
            start = j * limit_per_process
            if start >= length:
                break
            count = (
                (length - start) if j == n - 1 else min(limit_per_process, length - start)
            )

            p = Process(
                target=run_worker_process,
                kwargs={
                    "filepath": filepath,
                    "db_params": self.db_params,
                    "schema": schema,
                    "start": start,
                    "limit": count,
                    "batch_size": self.batch_size,
                },
            )
            processes.append(p)
            p.start()

        for p in processes:
            p.join()
