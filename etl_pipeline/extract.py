from __future__ import annotations

from pathlib import Path

from bson import decode_file_iter, json_util


def extract_bson_to_ndjson(bson_path: Path, out_path: Path) -> None:
    out_path.parent.mkdir(parents=True, exist_ok=True)
    with bson_path.open("rb") as source, out_path.open("w", encoding="utf-8", newline="") as target:
        for document in decode_file_iter(source):
            target.write(json_util.dumps(document))
            target.write("\n")
