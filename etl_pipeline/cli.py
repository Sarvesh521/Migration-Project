from __future__ import annotations

import argparse
import tempfile
from pathlib import Path
from typing import Dict, List

from .config import source_specs
from .extract import extract_bson_to_ndjson
from .load import create_tables, filter_csv_dir_for_strict_fks, fk_violations_from_csv, load_csv_dir
from .transform import merge_table_rows, transform_collection, write_csv_outputs


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="ETL pipeline from MongoDB BSON dump to SQL tables.")
    parser.add_argument("--dump-root", type=Path, default=Path("."), help="Root directory of Mongo dump.")
    parser.add_argument("--work-dir", type=Path, default=Path(".etl_work"), help="Directory for NDJSON intermediate files.")
    parser.add_argument("--output-dir", type=Path, default=Path("output/csv"), help="Directory for CSV outputs.")
    parser.add_argument("--db-uri", default="sqlite:///output/admissions_etl.db", help="SQLAlchemy DB URI for loading stage.")
    parser.add_argument("--extract-only", action="store_true", help="Run extract stage only.")
    parser.add_argument("--transform-only", action="store_true", help="Run transform stage only from NDJSON files.")
    parser.add_argument("--skip-load", action="store_true", help="Skip SQL load stage.")
    parser.add_argument("--recreate-schema", action="store_true", help="Drop and recreate SQL tables before loading.")
    parser.add_argument("--enforce-fks", action="store_true", help="Create and enforce SQL foreign key constraints.")
    parser.add_argument("--collections", default="", help="Comma-separated source names to process.")
    return parser.parse_args()


def run() -> None:
    args = parse_args()
    dump_root = args.dump_root.resolve()
    work_dir = args.work_dir.resolve()
    output_dir = args.output_dir.resolve()

    specs = source_specs(dump_root)
    if args.collections.strip():
        allowed = {x.strip() for x in args.collections.split(",") if x.strip()}
        specs = [s for s in specs if s.name in allowed]

    if not specs:
        raise ValueError("No collections selected. Check --collections values.")

    if not args.transform_only:
        for spec in specs:
            if not spec.bson_path.exists():
                print(f"[WARN] Missing source BSON, skipping: {spec.bson_path}")
                continue
            ndjson_out = work_dir / f"{spec.name}.ndjson"
            print(f"[EXTRACT] {spec.bson_path} -> {ndjson_out}")
            extract_bson_to_ndjson(spec.bson_path, ndjson_out)

        if args.extract_only:
            print("[DONE] Extract stage completed.")
            return

    transformed_chunks: List[Dict[str, List[dict]]] = []
    for spec in specs:
        ndjson_path = work_dir / f"{spec.name}.ndjson"
        if not ndjson_path.exists():
            print(f"[WARN] Missing NDJSON, skipping transform: {ndjson_path}")
            continue
        print(f"[TRANSFORM] {ndjson_path}")
        transformed_chunks.append(transform_collection(spec.name, ndjson_path))

    merged = merge_table_rows(transformed_chunks)
    written = write_csv_outputs(merged, output_dir)
    print(f"[TRANSFORM] Wrote {len(written)} CSV files to {output_dir}")

    if args.skip_load:
        print("[DONE] Skipped load stage.")
        return

    load_source_dir = output_dir
    if args.enforce_fks:
        reject_dir = output_dir.parent / "rejects"
        with tempfile.TemporaryDirectory(prefix="strict-csv-") as temp_dir:
            cleaned_dir = Path(temp_dir)
            reject_counts = filter_csv_dir_for_strict_fks(output_dir, cleaned_dir, reject_dir)

            if reject_counts:
                total_rejects = sum(reject_counts.values())
                print(f"[FK-CHECK] Rejected {total_rejects} row(s) across {len(reject_counts)} table(s). See {reject_dir}.")
                for table_name in sorted(reject_counts):
                    print(f"  - {table_name}: {reject_counts[table_name]} rejected row(s)")
            else:
                print("[FK-CHECK] No orphan references detected in CSV outputs.")

            load_source_dir = cleaned_dir
            print(f"[LOAD] Creating tables and loading cleaned CSV files into {args.db_uri}")
            create_tables(args.db_uri, recreate=args.recreate_schema, enforce_foreign_keys=True)
            load_csv_dir(args.db_uri, load_source_dir, truncate=True, enforce_foreign_keys=True)
            print("[DONE] ETL pipeline completed successfully.")
            return

    violations = fk_violations_from_csv(output_dir)
    if violations:
        print("[FK-CHECK] Found potential orphan references in transformed CSV files:")
        for item in violations:
            print(
                f"  - {item['child_table']}.{item['child_column']} -> "
                f"{item['parent_table']}.{item['parent_column']}: "
                f"{item['orphan_count']} orphan value(s), examples={item['examples']}"
            )
    else:
        print("[FK-CHECK] No orphan references detected in CSV outputs.")

    print(f"[LOAD] Creating tables and loading CSV files into {args.db_uri}")
    create_tables(args.db_uri, recreate=args.recreate_schema, enforce_foreign_keys=args.enforce_fks)
    load_csv_dir(args.db_uri, output_dir, truncate=True, enforce_foreign_keys=args.enforce_fks)
    print("[DONE] ETL pipeline completed successfully.")


if __name__ == "__main__":
    run()
