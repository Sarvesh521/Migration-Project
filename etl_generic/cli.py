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

    process_initiator = ProcessInitiator(
        db_uri=args.db_uri,
        num_workers=args.workers,
        batch_size=args.batch_size,
    )

    summary_stats = []

    for filepath in bson_files:
        collection_name = os.path.basename(filepath)[:-5]
        print(f"\n[+] Processing Collection/BSON: '{collection_name}' ({filepath})")

        # 1. Algorithm 1: Schema Analyzer
        print("  -> Step 1/3: Running Schema Analyzer (Algorithm 1)...")
        analyzer_start = time.time()
        analyzer = SchemaAnalyzer(collection_name=collection_name, max_depth=2)
        schema = analyzer.analyze_bson_file(filepath)
        analyzer_time = time.time() - analyzer_start

        # Save intermediate schema JSON file
        schema_json_path = os.path.join(args.schema_output_dir, f"{collection_name}.json")
        import json
        with open(schema_json_path, "w", encoding="utf-8") as sf:
            json.dump(schema, sf, indent=2)

        print(
            f"     Schema inferred in {analyzer_time:.2f}s across {len(schema['tables'])} table(s) (Saved to {schema_json_path}):"
        )
        for tname in schema["tables"].keys():
            print(f"       * Table: {tname}")

        # 2. Algorithm 2 & 3: Process Initiation & Worker ETL Load
        print("  -> Step 2/3: Initializing Schema & Spawning Worker Processes (Algorithm 2)...")
        if args.recreate_schema:
            process_initiator.initialize_schema(schema, recreate=True)

        print("  -> Step 3/3: Executing Concurrent Transformation & Loading (Algorithm 3)...")
        etl_start = time.time()
        process_initiator.start_etl_for_file(
            filepath=filepath,
            collection_name=collection_name,
            schema=schema,
        )
        etl_time = time.time() - etl_start

        summary_stats.append(
            {
                "collection": collection_name,
                "tables": list(schema["tables"].keys()),
                "analyzer_time": analyzer_time,
                "etl_time": etl_time,
            }
        )

    total_time = time.time() - start_time
    print("\n==================================================")
    print(" Migration Finished Successfully! Summary Report:  ")
    print("==================================================")
    for stat in summary_stats:
        print(
            f" Collection: {stat['collection']} | Tables: {', '.join(stat['tables'])} | Schema Time: {stat['analyzer_time']:.2f}s | Load Time: {stat['etl_time']:.2f}s"
        )
    print(f"Total Execution Time: {total_time:.2f} seconds")
    print("==================================================")


if __name__ == "__main__":
    run()
