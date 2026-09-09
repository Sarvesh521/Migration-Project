import datetime
import bson
from bson.objectid import ObjectId
from bson.decimal128 import Decimal128


def normalize_bson_val(val):
    """
    Recursively converts BSON specific types (ObjectId, Decimal128, Datetime, bytes)
    into standard Python types for JSON/SQL serialization.
    """
    if val is None:
        return None
    elif isinstance(val, ObjectId):
        return str(val)
    elif isinstance(val, Decimal128):
        return str(val)
    elif isinstance(val, datetime.datetime):
        # Format for MySQL DATETIME(6)
        return val.strftime("%Y-%m-%d %H:%M:%S.%f")
    elif isinstance(val, bytes):
        return val.hex()
    elif isinstance(val, dict):
        return {k: normalize_bson_val(v) for k, v in val.items()}
    elif isinstance(val, list):
        return [normalize_bson_val(item) for item in val]
    return val


def count_bson_file(filepath):
    """
    Counts total BSON documents in a file.
    """
    count = 0
    with open(filepath, "rb") as f:
        for _ in bson.decode_file_iter(f):
            count += 1
    return count


def read_bson_file(filepath, start=0, limit=None):
    """
    Generator yielding BSON documents from start index up to limit.
    """
    idx = 0
    yielded = 0
    with open(filepath, "rb") as f:
        for doc in bson.decode_file_iter(f):
            if idx < start:
                idx += 1
                continue
            if limit is not None and yielded >= limit:
                break
            yield doc
            idx += 1
            yielded += 1
