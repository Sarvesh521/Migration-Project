from __future__ import annotations

import csv
import json
import re
from collections import defaultdict
from pathlib import Path
from typing import Any, Dict, Iterable, List, Tuple

from .config import CHILD_TABLE_COLUMNS, ROOT_TABLE_COLUMNS


def _to_iso_datetime(value: Any) -> Any:
    if isinstance(value, dict):
        if "$date" in value:
            date_val = value["$date"]
            if isinstance(date_val, str):
                return date_val
            if isinstance(date_val, dict) and "$numberLong" in date_val:
                return date_val["$numberLong"]
    return value


def _normalize(value: Any) -> Any:
    if isinstance(value, dict):
        if "$oid" in value:
            return value["$oid"]
        if "$numberInt" in value:
            try:
                return int(value["$numberInt"])
            except Exception:
                return value["$numberInt"]
        if "$numberLong" in value:
            try:
                return int(value["$numberLong"])
            except Exception:
                return value["$numberLong"]
        if "$numberDouble" in value:
            try:
                return float(value["$numberDouble"])
            except Exception:
                return value["$numberDouble"]
        if "$date" in value:
            return _to_iso_datetime(value)
        return json.dumps(value, ensure_ascii=True)
    if isinstance(value, list):
        return json.dumps(value, ensure_ascii=True)
    return value


def _pick(doc: Dict[str, Any], *keys: str) -> Any:
    for key in keys:
        if key in doc and doc[key] not in (None, ""):
            return _normalize(doc[key])
    return None


def _extract_round_program_seats(doc: Dict[str, Any]) -> List[Dict[str, Any]]:
    seats_by_program: Dict[str, Dict[str, Any]] = defaultdict(dict)
    for key, value in doc.items():
        m = re.match(r"^(?P<program>[a-zA-Z0-9]+)_(?P<suffix>seats|count|cutoff)$", key)
        if not m:
            continue
        program = m.group("program").upper()
        suffix = m.group("suffix")
        if suffix == "seats":
            seats_by_program[program]["total_seats"] = _normalize(value)
        elif suffix == "count":
            seats_by_program[program]["filled_count"] = _normalize(value)
        elif suffix == "cutoff":
            seats_by_program[program]["cutoff_rank"] = _normalize(value)

    rows = []
    round_id = _pick(doc, "_id", "id")
    for program, values in seats_by_program.items():
        rows.append(
            {
                "round_mongo_id": round_id,
                "program_code": program,
                "total_seats": values.get("total_seats"),
                "filled_count": values.get("filled_count"),
                "cutoff_rank": values.get("cutoff_rank"),
            }
        )
    return rows


def transform_collection(collection_name: str, ndjson_path: Path) -> Dict[str, List[Dict[str, Any]]]:
    out: Dict[str, List[Dict[str, Any]]] = defaultdict(list)

    with ndjson_path.open("r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line:
                continue
            doc = json.loads(line)

            if collection_name == "users":
                out["users"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "user_name": _pick(doc, "user_name", "name"),
                        "user_email": _pick(doc, "user_email", "email"),
                        "mobile_number": _pick(doc, "mobile_number", "phone"),
                        "qualification": _pick(doc, "qualification"),
                    }
                )
            elif collection_name == "applications":
                out["applications"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "user_mongo_id": _pick(doc, "user_id", "user_mongo_id", "userId"),
                        "name": _pick(doc, "name"),
                        "status": _pick(doc, "status"),
                        "submitted_at": _pick(doc, "submitted_at", "g_creation_time", "created_at"),
                        "drive_id": _pick(doc, "drive_id", "driveId"),
                    }
                )
            elif collection_name == "offer_history":
                out["offer_history"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "application_mongo_id": _pick(doc, "application_id", "application_mongo_id", "applicationId"),
                        "offer_code": _pick(doc, "offer_code", "offer", "offer_id"),
                        "offered_at": _pick(doc, "offered_at", "g_creation_time", "created_at"),
                    }
                )
            elif collection_name == "rounds":
                out["rounds"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "name": _pick(doc, "name"),
                        "start_at": _pick(doc, "start_at", "start_date", "g_creation_time"),
                        "end_at": _pick(doc, "end_at", "end_date"),
                        "soft_delete": _pick(doc, "g_soft_delete", "soft_delete"),
                    }
                )
                out["round_program_seats"].extend(_extract_round_program_seats(doc))
            elif collection_name == "round_allocations":
                out["round_allocations"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "round_mongo_id": _pick(doc, "round_id", "round_mongo_id", "roundId"),
                        "application_mongo_id": _pick(doc, "application_id", "application_mongo_id", "applicationId"),
                        "seat_type": _pick(doc, "seat_type", "seatType", "program"),
                        "allocated_at": _pick(doc, "allocated_at", "g_creation_time", "created_at"),
                    }
                )
            elif collection_name == "round_payment_receipt_logs":
                out["round_payment_receipt_logs"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "application_mongo_id": _pick(doc, "application_id", "application_mongo_id", "applicationId"),
                        "payment_id": _pick(doc, "payment_id", "paymentId", "transaction_id"),
                        "amount": _pick(doc, "amount"),
                        "status": _pick(doc, "status"),
                        "logged_at": _pick(doc, "logged_at", "g_creation_time", "created_at"),
                    }
                )
            elif collection_name == "messages":
                msg_id = _pick(doc, "_id", "id")
                out["messages"].append(
                    {
                        "mongo_id": msg_id,
                        "from_mongo_id": _pick(doc, "from_id", "from_mongo_id", "fromUserId", "sender_id"),
                        "to_mongo_id": _pick(doc, "to_id", "to_mongo_id", "toUserId", "receiver_id"),
                        "subject": _pick(doc, "subject"),
                        "body": _pick(doc, "body", "message"),
                        "sent_at": _pick(doc, "sent_at", "g_creation_time", "created_at"),
                    }
                )
                attachments = doc.get("attachments")
                if isinstance(attachments, list):
                    for item in attachments:
                        if not isinstance(item, dict):
                            continue
                        out["message_attachments"].append(
                            {
                                "message_mongo_id": msg_id,
                                "file_mongo_id": _normalize(item.get("file_id") or item.get("id")),
                                "file_name": _normalize(item.get("file_name") or item.get("name")),
                                "file_path": _normalize(item.get("file_path") or item.get("path")),
                            }
                        )
            elif collection_name == "drive":
                out["drive"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "owner_mongo_id": _pick(doc, "owner_id", "user_id", "uploaded_by"),
                        "file_name": _pick(doc, "name", "file_name"),
                        "file_size": _pick(doc, "file_size", "size"),
                        "content_type": _pick(doc, "content_type", "mime_type"),
                        "file_path": _pick(doc, "filePath", "file_path", "path"),
                        "file_url": _pick(doc, "file_url", "url"),
                        "uploaded_at": _pick(doc, "uploaded_at", "g_creation_time", "created_at"),
                    }
                )
            elif collection_name == "documents":
                doc_id = _pick(doc, "_id", "id")
                out["documents"].append(
                    {
                        "mongo_id": doc_id,
                        "name": _pick(doc, "name"),
                        "file_path": _pick(doc, "filePath", "file_path", "path"),
                        "uploaded_by": _pick(doc, "uploadedBy", "uploaded_by", "user_id"),
                        "file_size": _pick(doc, "fileSize", "file_size", "size"),
                        "content_type": _pick(doc, "contentType", "content_type", "mime_type"),
                        "uploaded_at": _pick(doc, "uploaded_at", "g_creation_time", "created_at"),
                    }
                )
                tags = doc.get("tags")
                if isinstance(tags, list):
                    for tag in tags:
                        out["document_tags"].append(
                            {
                                "document_mongo_id": doc_id,
                                "tag": _normalize(tag),
                            }
                        )
            elif collection_name == "audit_log":
                out["audit_log"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "resource_type": _pick(doc, "resource_type"),
                        "resource_id": _pick(doc, "resource_id"),
                        "action": _pick(doc, "resource_action", "action"),
                        "user_mongo_id": _pick(doc, "user_id", "user_mongo_id"),
                        "created_at": _pick(doc, "g_creation_time", "created_at"),
                    }
                )
            elif collection_name == "checkpoints":
                out["checkpoints"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "thread_id": _pick(doc, "thread_id"),
                        "checkpoint_ns": _pick(doc, "checkpoint_ns"),
                        "checkpoint_id": _pick(doc, "checkpoint_id"),
                        "task_id": _pick(doc, "task_id"),
                        "idx": _pick(doc, "idx"),
                        "created_at": _pick(doc, "created_at", "ts", "g_creation_time"),
                    }
                )
            elif collection_name == "ai_models_config":
                out["ai_models_config"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "model_name": _pick(doc, "model_name", "name"),
                        "provider": _pick(doc, "provider"),
                        "model_version": _pick(doc, "model_version", "version"),
                        "active": _pick(doc, "active", "enabled"),
                    }
                )
            elif collection_name == "role_resource_permission":
                permission = doc.get("permission")
                out["role_resource_permission"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "role": _pick(doc, "role"),
                        "resource": _pick(doc, "resource"),
                        "permission": _normalize(permission),
                    }
                )
            elif collection_name == "queries":
                out["queries"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "user_mongo_id": _pick(doc, "user_id", "user_mongo_id"),
                        "query_text": _pick(doc, "query_text", "query"),
                        "executed_at": _pick(doc, "executed_at", "g_creation_time", "created_at"),
                        "query_status": _pick(doc, "status", "query_status"),
                    }
                )
            elif collection_name == "withdrawals":
                out["withdrawals"].append(
                    {
                        "mongo_id": _pick(doc, "_id", "id"),
                        "user_mongo_id": _pick(doc, "user_id", "user_mongo_id"),
                        "amount": _pick(doc, "amount"),
                        "status": _pick(doc, "status"),
                        "created_at": _pick(doc, "created_at", "g_creation_time"),
                    }
                )

    return out


def _write_table_csv(table_name: str, rows: List[Dict[str, Any]], out_dir: Path) -> Path:
    root_columns = ROOT_TABLE_COLUMNS.get(table_name)
    child_columns = CHILD_TABLE_COLUMNS.get(table_name)
    columns = root_columns if root_columns is not None else child_columns
    if columns is None:
        raise ValueError(f"Unknown table name: {table_name}")

    out_dir.mkdir(parents=True, exist_ok=True)
    out_path = out_dir / f"{table_name}.csv"
    with out_path.open("w", encoding="utf-8", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=columns)
        writer.writeheader()
        for row in rows:
            writer.writerow({c: row.get(c) for c in columns})
    return out_path


def write_csv_outputs(table_rows: Dict[str, List[Dict[str, Any]]], out_dir: Path) -> List[Path]:
    written = []
    for table_name, rows in sorted(table_rows.items()):
        written.append(_write_table_csv(table_name, rows, out_dir))
    return written


def merge_table_rows(chunks: Iterable[Dict[str, List[Dict[str, Any]]]]) -> Dict[str, List[Dict[str, Any]]]:
    merged: Dict[str, List[Dict[str, Any]]] = defaultdict(list)
    for chunk in chunks:
        for table_name, rows in chunk.items():
            merged[table_name].extend(rows)
    return merged
