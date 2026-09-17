import os
import argparse
import time
from etl_generic.schema_analyzer import SchemaAnalyzer
from etl_generic.process_initiator import ProcessInitiator, parse_db_uri, get_db_connection


def discover_bson_files(dump_path):
    if os.path.isfile(dump_path) and dump_path.endswith(".bson"):
        return [dump_path]

    bson_files = []
    if os.path.isdir(dump_path):
        for root, _, files in os.walk(dump_path):
            for file in sorted(files):
                if file.endswith(".bson"):
                    bson_files.append(os.path.join(root, file))
    return bson_files


def run():
    parser = argparse.ArgumentParser(
        description="Automatic MongoDB BSON to MySQL ETL Migration based on Aftab et al. (2020)"
    )
    parser.add_argument(
        "--dump-dir",
        default="rasp_db",
        help="Path to directory containing .bson dump files or path to a single .bson file",
    )
    parser.add_argument(
        "--db-uri",
        default="mysql+pymysql://admissions:admissions@mysql:3306/admissions",
        help="Target MySQL DB URI",
    )
    parser.add_argument(
        "--workers",
        type=int,
        default=4,
        help="Number of concurrent worker processes (n)",
    )
    parser.add_argument(
        "--batch-size",
        type=int,
        default=500,
        help="Batch size for database insertions",
    )
    parser.add_argument(
        "--schema-output-dir",
        default="output_generic",
        help="Directory to save intermediate inferred schema JSON files",
    )
    parser.add_argument(
        "--gui-port",
        type=int,
        default=5000,
        help="Port for Web GUI Schema Studio (default: 5000)",
    )
    parser.add_argument(
        "--headless",
        action="store_true",
        help="Bypass Web GUI interactive schema approval and run automatically",
    )
    parser.add_argument(
        "--recreate-schema",
        action="store_true",
        help="Recreate MySQL tables if they already exist",
    )

    args = parser.parse_args()

    os.makedirs(args.schema_output_dir, exist_ok=True)

    start_time = time.time()
    print("==================================================")
    print(" Starting Automatic MongoDB -> MySQL Migration    ")
    print(" Methodology: Aftab et al. (2020) ETL Algorithm  ")
    print("==================================================")
    print(f" Source Dump Path   : {args.dump_dir}")
    print(f" Schema Output Dir  : {args.schema_output_dir}")
    print(f" Target DB URI      : {args.db_uri}")
    print(f" Web GUI Port       : {args.gui_port}")
    print(f" Headless Mode      : {args.headless}")
    print(f" Worker Processes   : {args.workers}")
    print(f" Batch Size         : {args.batch_size}")
    print(f" Recreate Schema    : {args.recreate_schema}")
    print("--------------------------------------------------")

    bson_files = discover_bson_files(args.dump_dir)
    if not bson_files:
        print(f"Error: No .bson files found in '{args.dump_dir}'")
        return

    print(f"Discovered {len(bson_files)} BSON file(s) for migration:")
    for f in bson_files:
        print(f"  - {f}")

    # 1. Step 1: Run Algorithm 1 (Schema Analyzer) across all collections
    print("\n[+] Step 1/3: Running Schema Analyzer (Algorithm 1)...")
    combined_schema = {"collection_name": "all_collections", "tables": {}}
    schemas_by_file = {}

    import json
    for filepath in bson_files:
        collection_name = os.path.basename(filepath)[:-5]
        analyzer = SchemaAnalyzer(collection_name=collection_name, max_depth=2)
        schema = analyzer.analyze_bson_file(filepath)
        schemas_by_file[filepath] = schema
        for tkey, tbl in schema["tables"].items():
            combined_schema["tables"][tkey] = tbl

        schema_json_path = os.path.join(args.schema_output_dir, f"{collection_name}.json")
        with open(schema_json_path, "w", encoding="utf-8") as sf:
            json.dump(schema, sf, indent=2)

    combined_inferred_path = os.path.join(args.schema_output_dir, "inferred_schema.json")
    with open(combined_inferred_path, "w", encoding="utf-8") as csf:
        json.dump(combined_schema, csf, indent=2)

    print(f"    Inferred schema across {len(combined_schema['tables'])} table(s) saved to {args.schema_output_dir}/")

    # 2. Step 2: Web GUI Schema Review Layer (unless --headless)
    approved_schema = combined_schema
    if not args.headless:
        from etl_generic.web_gui import wait_for_user_approval
        approved_schema = wait_for_user_approval(combined_schema, port=args.gui_port)

    # Overwrite JSON schema files in output_generic/ with user-approved modifications
    approved_json_path = os.path.join(args.schema_output_dir, "approved_schema.json")
    with open(approved_json_path, "w", encoding="utf-8") as af:
        json.dump(approved_schema, af, indent=2)

    with open(combined_inferred_path, "w", encoding="utf-8") as csf:
        json.dump(approved_schema, csf, indent=2)

    for filepath in bson_files:
        collection_name = os.path.basename(filepath)[:-5]
        file_schema = schemas_by_file[filepath]
        # Sync file_schema tables with approved schema modifications
        for tkey in list(file_schema["tables"].keys()):
            if tkey in approved_schema["tables"]:
                file_schema["tables"][tkey] = approved_schema["tables"][tkey]
        schema_json_path = os.path.join(args.schema_output_dir, f"{collection_name}.json")
        with open(schema_json_path, "w", encoding="utf-8") as sf:
            json.dump(file_schema, sf, indent=2)

    process_initiator = ProcessInitiator(
        db_uri=args.db_uri,
        num_workers=args.workers,
        batch_size=args.batch_size,
    )

    # 3. Step 3: Execute Algorithm 2 (Process Initiator DDL) ONCE for all tables
    print("\n[+] Step 2 & 3: Initializing Tables & Loading Records (Algorithm 2 & 3)...")
    process_initiator.initialize_schema(approved_schema, recreate=args.recreate_schema)

    summary_stats = []

    for filepath in bson_files:
        collection_name = os.path.basename(filepath)[:-5]
        file_schema = schemas_by_file[filepath]

        print(f"\n  -> Processing Collection: '{collection_name}'")

        etl_start = time.time()
        process_initiator.start_etl_for_file(
            filepath=filepath,
            collection_name=collection_name,
            schema=file_schema,
        )
        etl_time = time.time() - etl_start

        summary_stats.append(
            {
                "collection": collection_name,
                "tables": [t for t in file_schema["tables"].keys() if not file_schema["tables"][t].get("excluded")],
                "etl_time": etl_time,
            }
        )

    total_time = time.time() - start_time
    print("\n==================================================")
    print(" Migration Finished Successfully! Summary Report:  ")
    print("==================================================")
    for stat in summary_stats:
        print(
            f" Collection: {stat['collection']} | Active Tables: {', '.join(stat['tables'])} | Load Time: {stat['etl_time']:.2f}s"
        )
    print(f"Total Execution Time: {total_time:.2f} seconds")
    print("==================================================")


if __name__ == "__main__":
    run()
