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
        Creates target MySQL tables for the collection and all child tables.
        """
        conn = get_db_connection(self.db_params)
        try:
            with conn.cursor() as cursor:
                try:
                    cursor.execute("SET SESSION innodb_strict_mode=0;")
                except Exception:
                    pass
                # Order tables so child tables are created after parent tables (or dropped first)
                tables = schema["tables"]
                main_table = schema["collection_name"]

                if recreate:
                    # Drop child tables first, then main table
                    for tbl_name in reversed(list(tables.keys())):
                        cursor.execute(f"DROP TABLE IF EXISTS `{tbl_name}`")

                # Create parent table first, then child tables
                ordered_tbl_names = [main_table] + [
                    t for t in tables.keys() if t != main_table
                ]

                for tbl_name in ordered_tbl_names:
                    tbl_info = tables[tbl_name]
                    col_defs = []

                    for col_name, cinfo in tbl_info["columns"].items():
                        sql_type = cinfo["sql_type"]
                        col_defs.append(f"`{col_name}` {sql_type}")

                    # Foreign key constraint for child tables
                    fk_clause = ""
                    if tbl_info["is_child"]:
                        parent_tbl = tbl_info["parent_table"]
                        parent_fk_col = tbl_info["parent_fk_col"]
                        parent_pk = tables[parent_tbl]["primary_key"]
                        fk_clause = f", FOREIGN KEY (`{parent_fk_col}`) REFERENCES `{parent_tbl}`(`{parent_pk}`) ON DELETE CASCADE"

                    create_query = f"CREATE TABLE IF NOT EXISTS `{tbl_name}` ({', '.join(col_defs)}{fk_clause}) ENGINE=InnoDB ROW_FORMAT=DYNAMIC DEFAULT CHARSET=utf8mb4;"
                    cursor.execute(create_query)
        finally:
            conn.close()

    def start_etl_for_file(self, filepath, collection_name, schema):
        """
        Initiates multi-process ETL for a single BSON file.
        """
        self.initialize_schema(schema)

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
