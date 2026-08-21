from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path
from typing import Dict, List


@dataclass(frozen=True)
class SourceSpec:
    name: str
    bson_path: Path


ROOT_TABLE_COLUMNS: Dict[str, List[str]] = {
    "users": ["mongo_id", "user_name", "user_email", "mobile_number", "qualification"],
    "applications": ["mongo_id", "user_mongo_id", "name", "status", "submitted_at", "drive_id"],
    "offer_history": ["mongo_id", "application_mongo_id", "offer_code", "offered_at"],
    "rounds": ["mongo_id", "name", "start_at", "end_at", "soft_delete"],
    "round_allocations": ["mongo_id", "round_mongo_id", "application_mongo_id", "seat_type", "allocated_at"],
    "round_payment_receipt_logs": ["mongo_id", "application_mongo_id", "payment_id", "amount", "status", "logged_at"],
    "messages": ["mongo_id", "from_mongo_id", "to_mongo_id", "subject", "body", "sent_at"],
    "drive": ["mongo_id", "owner_mongo_id", "file_name", "file_size", "content_type", "file_path", "file_url", "uploaded_at"],
    "documents": ["mongo_id", "name", "file_path", "uploaded_by", "file_size", "content_type", "uploaded_at"],
    "audit_log": ["mongo_id", "resource_type", "resource_id", "action", "user_mongo_id", "created_at"],
    "checkpoints": ["mongo_id", "thread_id", "checkpoint_ns", "checkpoint_id", "task_id", "idx", "created_at"],
    "ai_models_config": ["mongo_id", "model_name", "provider", "model_version", "active"],
    "role_resource_permission": ["mongo_id", "role", "resource", "permission"],
    "queries": ["mongo_id", "user_mongo_id", "query_text", "executed_at", "query_status"],
    "withdrawals": ["mongo_id", "user_mongo_id", "amount", "status", "created_at"],
}

CHILD_TABLE_COLUMNS: Dict[str, List[str]] = {
    "message_attachments": ["message_mongo_id", "file_mongo_id", "file_name", "file_path"],
    "document_tags": ["document_mongo_id", "tag"],
    "round_program_seats": ["round_mongo_id", "program_code", "total_seats", "filled_count", "cutoff_rank"],
}


def source_specs(dump_root: Path) -> List[SourceSpec]:
    return [
        SourceSpec("users", dump_root / "rasp_db" / "users.bson"),
        SourceSpec("applications", dump_root / "rasp_db" / "applications.bson"),
        SourceSpec("offer_history", dump_root / "rasp_db" / "offer_history.bson"),
        SourceSpec("rounds", dump_root / "rasp_db" / "rounds.bson"),
        SourceSpec("round_allocations", dump_root / "rasp_db" / "round_allocations.bson"),
        SourceSpec("round_payment_receipt_logs", dump_root / "rasp_db" / "round_payment_receipt_logs.bson"),
        SourceSpec("messages", dump_root / "rasp_db" / "message.bson"),
        SourceSpec("drive", dump_root / "rasp_db" / "drive.bson"),
        SourceSpec("queries", dump_root / "rasp_db" / "query.bson"),
        SourceSpec("withdrawals", dump_root / "rasp_db" / "withdrawals.bson"),
        SourceSpec("documents", dump_root / "documentManagementSystem" / "document.bson"),
        SourceSpec("audit_log", dump_root / "DB_AUDIT_LOG" / "audit_log.bson"),
        SourceSpec("checkpoints", dump_root / "ai_workflows" / "checkpoints.bson"),
        SourceSpec("ai_models_config", dump_root / "ai_workflows" / "ai_models_config.bson"),
        SourceSpec("role_resource_permission", dump_root / "DB_ACCOUNT" / "role_resource_permission.bson"),
    ]
