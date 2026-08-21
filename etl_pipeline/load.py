from __future__ import annotations

import csv
import re
from datetime import datetime, timezone
from pathlib import Path
from typing import Dict, List, Tuple

from sqlalchemy import create_engine, text

from .config import CHILD_TABLE_COLUMNS, ROOT_TABLE_COLUMNS


DDL: Dict[str, str] = {
    "users": """
        CREATE TABLE IF NOT EXISTS users (
            mongo_id VARCHAR(64) PRIMARY KEY,
            user_name TEXT,
            user_email TEXT,
            mobile_number TEXT,
            qualification TEXT
        )
    """,
    "applications": """
        CREATE TABLE IF NOT EXISTS applications (
            mongo_id VARCHAR(64) PRIMARY KEY,
            user_mongo_id VARCHAR(64),
            name TEXT,
            status TEXT,
            submitted_at DATETIME,
            drive_id VARCHAR(64),
            FOREIGN KEY (user_mongo_id) REFERENCES users(mongo_id)
        )
    """,
    "offer_history": """
        CREATE TABLE IF NOT EXISTS offer_history (
            mongo_id VARCHAR(64) PRIMARY KEY,
            application_mongo_id VARCHAR(64),
            offer_code TEXT,
            offered_at DATETIME,
            FOREIGN KEY (application_mongo_id) REFERENCES applications(mongo_id)
        )
    """,
    "rounds": """
        CREATE TABLE IF NOT EXISTS rounds (
            mongo_id VARCHAR(64) PRIMARY KEY,
            name TEXT,
            start_at DATETIME,
            end_at DATETIME,
            soft_delete BOOLEAN
        )
    """,
    "round_program_seats": """
        CREATE TABLE IF NOT EXISTS round_program_seats (
            round_mongo_id VARCHAR(64),
            program_code VARCHAR(32),
            total_seats INTEGER,
            filled_count INTEGER,
            cutoff_rank DECIMAL(18,4),
            PRIMARY KEY (round_mongo_id, program_code),
            FOREIGN KEY (round_mongo_id) REFERENCES rounds(mongo_id)
        )
    """,
    "round_allocations": """
        CREATE TABLE IF NOT EXISTS round_allocations (
            mongo_id VARCHAR(64) PRIMARY KEY,
            round_mongo_id VARCHAR(64),
            application_mongo_id VARCHAR(64),
            seat_type TEXT,
            allocated_at DATETIME,
            FOREIGN KEY (round_mongo_id) REFERENCES rounds(mongo_id),
            FOREIGN KEY (application_mongo_id) REFERENCES applications(mongo_id)
        )
    """,
    "round_payment_receipt_logs": """
        CREATE TABLE IF NOT EXISTS round_payment_receipt_logs (
            mongo_id VARCHAR(64) PRIMARY KEY,
            application_mongo_id VARCHAR(64),
            payment_id TEXT,
            amount DECIMAL(18,4),
            status TEXT,
            logged_at DATETIME,
            FOREIGN KEY (application_mongo_id) REFERENCES applications(mongo_id)
        )
    """,
    "messages": """
        CREATE TABLE IF NOT EXISTS messages (
            mongo_id VARCHAR(64) PRIMARY KEY,
            from_mongo_id VARCHAR(64),
            to_mongo_id VARCHAR(64),
            subject TEXT,
            body LONGTEXT,
            sent_at DATETIME,
            FOREIGN KEY (from_mongo_id) REFERENCES users(mongo_id),
            FOREIGN KEY (to_mongo_id) REFERENCES users(mongo_id)
        )
    """,
    "message_attachments": """
        CREATE TABLE IF NOT EXISTS message_attachments (
            message_mongo_id VARCHAR(64),
            file_mongo_id VARCHAR(64),
            file_name TEXT,
            file_path TEXT,
            PRIMARY KEY (message_mongo_id, file_mongo_id),
            FOREIGN KEY (message_mongo_id) REFERENCES messages(mongo_id),
            FOREIGN KEY (file_mongo_id) REFERENCES drive(mongo_id)
        )
    """,
    "drive": """
        CREATE TABLE IF NOT EXISTS drive (
            mongo_id VARCHAR(64) PRIMARY KEY,
            owner_mongo_id VARCHAR(64),
            file_name TEXT,
            file_size BIGINT,
            content_type TEXT,
            file_path TEXT,
            file_url TEXT,
            uploaded_at DATETIME,
            FOREIGN KEY (owner_mongo_id) REFERENCES users(mongo_id)
        )
    """,
    "documents": """
        CREATE TABLE IF NOT EXISTS documents (
            mongo_id VARCHAR(64) PRIMARY KEY,
            name TEXT,
            file_path TEXT,
            uploaded_by VARCHAR(64),
            file_size BIGINT,
            content_type TEXT,
            uploaded_at DATETIME,
            FOREIGN KEY (uploaded_by) REFERENCES users(mongo_id)
        )
    """,
    "document_tags": """
        CREATE TABLE IF NOT EXISTS document_tags (
            document_mongo_id VARCHAR(64),
            tag VARCHAR(255),
            PRIMARY KEY (document_mongo_id, tag),
            FOREIGN KEY (document_mongo_id) REFERENCES documents(mongo_id)
        )
    """,
    "audit_log": """
        CREATE TABLE IF NOT EXISTS audit_log (
            mongo_id VARCHAR(64) PRIMARY KEY,
            resource_type TEXT,
            resource_id VARCHAR(64),
            action TEXT,
            user_mongo_id VARCHAR(64),
            created_at DATETIME,
            FOREIGN KEY (user_mongo_id) REFERENCES users(mongo_id)
        )
    """,
    "checkpoints": """
        CREATE TABLE IF NOT EXISTS checkpoints (
            mongo_id VARCHAR(64) PRIMARY KEY,
            thread_id TEXT,
            checkpoint_ns TEXT,
            checkpoint_id TEXT,
            task_id TEXT,
            idx INTEGER,
            created_at DATETIME
        )
    """,
    "ai_models_config": """
        CREATE TABLE IF NOT EXISTS ai_models_config (
            mongo_id VARCHAR(64) PRIMARY KEY,
            model_name TEXT,
            provider TEXT,
            model_version TEXT,
            active BOOLEAN
        )
    """,
    "role_resource_permission": """
        CREATE TABLE IF NOT EXISTS role_resource_permission (
            mongo_id VARCHAR(64) PRIMARY KEY,
            role TEXT,
            resource TEXT,
            permission TEXT
        )
    """,
    "queries": """
        CREATE TABLE IF NOT EXISTS queries (
            mongo_id VARCHAR(64) PRIMARY KEY,
            user_mongo_id VARCHAR(64),
            query_text LONGTEXT,
            executed_at DATETIME,
            query_status TEXT,
            FOREIGN KEY (user_mongo_id) REFERENCES users(mongo_id)
        )
    """,
    "withdrawals": """
        CREATE TABLE IF NOT EXISTS withdrawals (
            mongo_id VARCHAR(64) PRIMARY KEY,
            user_mongo_id VARCHAR(64),
            amount DECIMAL(18,4),
            status TEXT,
            created_at DATETIME,
            FOREIGN KEY (user_mongo_id) REFERENCES users(mongo_id)
        )
    """,
}


CHILD_FIRST_TABLE_ORDER = [
    "message_attachments",
    "document_tags",
    "round_program_seats",
    "round_allocations",
    "round_payment_receipt_logs",
    "offer_history",
    "queries",
    "withdrawals",
    "audit_log",
    "documents",
    "messages",
    "applications",
    "drive",
    "users",
    "rounds",
    "checkpoints",
    "ai_models_config",
    "role_resource_permission",
]


FK_RULES: List[Tuple[str, str, str, str]] = [
    ("applications", "user_mongo_id", "users", "mongo_id"),
    ("offer_history", "application_mongo_id", "applications", "mongo_id"),
    ("round_program_seats", "round_mongo_id", "rounds", "mongo_id"),
    ("round_allocations", "round_mongo_id", "rounds", "mongo_id"),
    ("round_allocations", "application_mongo_id", "applications", "mongo_id"),
    ("round_payment_receipt_logs", "application_mongo_id", "applications", "mongo_id"),
    ("messages", "from_mongo_id", "users", "mongo_id"),
    ("messages", "to_mongo_id", "users", "mongo_id"),
    ("message_attachments", "message_mongo_id", "messages", "mongo_id"),
    ("message_attachments", "file_mongo_id", "drive", "mongo_id"),
    ("drive", "owner_mongo_id", "users", "mongo_id"),
    ("documents", "uploaded_by", "users", "mongo_id"),
    ("document_tags", "document_mongo_id", "documents", "mongo_id"),
    ("audit_log", "user_mongo_id", "users", "mongo_id"),
    ("queries", "user_mongo_id", "users", "mongo_id"),
    ("withdrawals", "user_mongo_id", "users", "mongo_id"),
]


def _enable_foreign_keys_if_sqlite(conn, db_uri: str, enforce_foreign_keys: bool) -> None:
    if db_uri.startswith("sqlite"):
        conn.execute(text(f"PRAGMA foreign_keys = {'ON' if enforce_foreign_keys else 'OFF'}"))


def _ddl_without_fk(ddl_sql: str) -> str:
    lines = ddl_sql.splitlines()
    kept = [line for line in lines if "FOREIGN KEY" not in line]
    joined = "\n".join(kept)
    # Remove trailing comma before closing parenthesis.
    joined = re.sub(r",\s*\)\s*$", "\n        )", joined, flags=re.MULTILINE)
    return joined


def _get_ddl_map(enforce_foreign_keys: bool) -> Dict[str, str]:
    if enforce_foreign_keys:
        return DDL
    return {table: _ddl_without_fk(sql) for table, sql in DDL.items()}


def _normalize_insert_row(row: dict) -> dict:
    normalized = {}
    for key, value in row.items():
        if value == "":
            normalized[key] = None
        elif key.endswith("_at") and isinstance(value, str):
            if re.fullmatch(r"\d{10,13}", value):
                timestamp = int(value)
                if len(value) == 13:
                    timestamp /= 1000.0
                dt = datetime.fromtimestamp(timestamp, tz=timezone.utc)
                normalized[key] = dt.replace(tzinfo=None).strftime("%Y-%m-%d %H:%M:%S")
            else:
                try:
                    dt = datetime.fromisoformat(value.replace("Z", "+00:00"))
                    if dt.tzinfo is not None:
                        dt = dt.astimezone(timezone.utc).replace(tzinfo=None)
                    normalized[key] = dt.strftime("%Y-%m-%d %H:%M:%S")
                except ValueError:
                    normalized[key] = value
        elif key in {"soft_delete", "active"}:
            if isinstance(value, str):
                lowered = value.strip().lower()
                if lowered in {"y", "yes", "true", "1"}:
                    normalized[key] = True
                elif lowered in {"n", "no", "false", "0"}:
                    normalized[key] = False
                else:
                    normalized[key] = value
            else:
                normalized[key] = bool(value)
        else:
            normalized[key] = value
    return normalized


def _read_csv_rows(csv_path: Path) -> List[dict]:
    with csv_path.open("r", encoding="utf-8", newline="") as f:
        return list(csv.DictReader(f))


def create_tables(db_uri: str, recreate: bool = False, enforce_foreign_keys: bool = True) -> None:
    engine = create_engine(db_uri)
    with engine.begin() as conn:
        _enable_foreign_keys_if_sqlite(conn, db_uri, enforce_foreign_keys)

        ddl_map = _get_ddl_map(enforce_foreign_keys)

        if recreate:
            for table in CHILD_FIRST_TABLE_ORDER:
                conn.execute(text(f"DROP TABLE IF EXISTS {table}"))

        for table in CHILD_FIRST_TABLE_ORDER[::-1]:
            conn.execute(text(ddl_map[table]))


def load_csv_dir(db_uri: str, csv_dir: Path, truncate: bool = True, enforce_foreign_keys: bool = True) -> None:
    engine = create_engine(db_uri)

    ordered_tables = list(ROOT_TABLE_COLUMNS.keys()) + list(CHILD_TABLE_COLUMNS.keys())
    with engine.begin() as conn:
        _enable_foreign_keys_if_sqlite(conn, db_uri, enforce_foreign_keys)

        if truncate:
            for table in CHILD_FIRST_TABLE_ORDER:
                conn.execute(text(f"DELETE FROM {table}"))

        for table in ordered_tables:
            csv_path = csv_dir / f"{table}.csv"
            if not csv_path.exists():
                continue

            rows = _read_csv_rows(csv_path)
            if not rows:
                continue

            columns = rows[0].keys()
            placeholders = ", ".join([f":{c}" for c in columns])
            sql = text(f"INSERT INTO {table} ({', '.join(columns)}) VALUES ({placeholders})")
            conn.execute(sql, [_normalize_insert_row(row) for row in rows])


def fk_violations_from_csv(csv_dir: Path) -> List[dict]:
    violations: List[dict] = []

    parent_keys: Dict[Tuple[str, str], set] = {}
    for _, _, parent_table, parent_col in FK_RULES:
        key = (parent_table, parent_col)
        if key in parent_keys:
            continue
        csv_path = csv_dir / f"{parent_table}.csv"
        values = set()
        if csv_path.exists():
            rows = _read_csv_rows(csv_path)
            for row in rows:
                val = row.get(parent_col)
                if val not in (None, ""):
                    values.add(val)
        parent_keys[key] = values

    for child_table, child_col, parent_table, parent_col in FK_RULES:
        csv_path = csv_dir / f"{child_table}.csv"
        if not csv_path.exists():
            continue

        parent_set = parent_keys[(parent_table, parent_col)]
        orphan_count = 0
        examples: List[str] = []

        for row in _read_csv_rows(csv_path):
            val = row.get(child_col)
            if val in (None, ""):
                continue
            if val not in parent_set:
                orphan_count += 1
                if len(examples) < 5:
                    examples.append(val)

        if orphan_count > 0:
            violations.append(
                {
                    "child_table": child_table,
                    "child_column": child_col,
                    "parent_table": parent_table,
                    "parent_column": parent_col,
                    "orphan_count": orphan_count,
                    "examples": examples,
                }
            )

    return violations


def filter_csv_dir_for_strict_fks(csv_dir: Path, cleaned_dir: Path, reject_dir: Path) -> Dict[str, int]:
    cleaned_dir.mkdir(parents=True, exist_ok=True)
    reject_dir.mkdir(parents=True, exist_ok=True)

    parent_keys: Dict[Tuple[str, str], set] = {}
    reject_counts: Dict[str, int] = {}
    ordered_tables = list(ROOT_TABLE_COLUMNS.keys()) + list(CHILD_TABLE_COLUMNS.keys())

    for table in ordered_tables:
        csv_path = csv_dir / f"{table}.csv"
        if not csv_path.exists():
            continue

        rows = _read_csv_rows(csv_path)
        if not rows:
            continue

        incoming_rules = [rule for rule in FK_RULES if rule[0] == table]
        reject_rows: List[dict] = []
        valid_rows: List[dict] = []

        for source_row_number, row in enumerate(rows, start=2):
            reasons: List[str] = []
            for _, child_col, parent_table, parent_col in incoming_rules:
                value = row.get(child_col)
                if value in (None, ""):
                    continue
                parent_set = parent_keys.get((parent_table, parent_col), set())
                if value not in parent_set:
                    reasons.append(f"{child_col}={value} missing {parent_table}.{parent_col}")

            if reasons:
                reject_row = dict(row)
                reject_row["_reject_reason"] = "; ".join(reasons)
                reject_row["_source_row_number"] = str(source_row_number)
                reject_rows.append(reject_row)
            else:
                valid_rows.append(row)

        columns = list(rows[0].keys())
        cleaned_path = cleaned_dir / f"{table}.csv"
        with cleaned_path.open("w", encoding="utf-8", newline="") as f:
            writer = csv.DictWriter(f, fieldnames=columns)
            writer.writeheader()
            for row in valid_rows:
                writer.writerow({c: row.get(c) for c in columns})

        if reject_rows:
            reject_path = reject_dir / f"{table}.csv"
            reject_columns = columns + ["_reject_reason", "_source_row_number"]
            with reject_path.open("w", encoding="utf-8", newline="") as f:
                writer = csv.DictWriter(f, fieldnames=reject_columns)
                writer.writeheader()
                for row in reject_rows:
                    writer.writerow({c: row.get(c) for c in reject_columns})
            reject_counts[table] = len(reject_rows)

        for _, _, parent_table, parent_col in FK_RULES:
            if parent_table != table:
                continue
            key = (parent_table, parent_col)
            parent_keys.setdefault(key, set())
            for row in valid_rows:
                value = row.get(parent_col)
                if value not in (None, ""):
                    parent_keys[key].add(value)

    return reject_counts
