# ETL Pipeline Overview

## What This Task Was

The goal of this project was to migrate a MongoDB export into a relational MySQL schema, while preserving referential integrity as much as possible and making the process reproducible from Docker.

The source data in this repository is a set of MongoDB dump folders containing BSON files. The ETL pipeline reads those BSON files, converts MongoDB documents into flat relational rows, writes the rows into CSV files, filters out rows that would violate foreign-key constraints, and then loads the valid rows into MySQL.

The final result is a MySQL database that can be inspected with a normal SQL client and queried like a relational system.

## High-Level Flow

The migration runs in four stages:

1. **Extract**  
   Read BSON documents from the MongoDB dump folders and convert them to NDJSON.

2. **Transform**  
   Map MongoDB document fields into relational table columns, flatten nested structures, and normalize arrays into child tables.

3. **Validate**  
   Check foreign-key relationships and separate valid rows from invalid rows. Invalid rows are written to reject CSV files for review.

4. **Load**  
   Create MySQL tables and insert the validated rows with foreign keys enabled.

In practice, the full pipeline is run with Docker using:

```bash
docker compose up -d mysql
docker compose run --rm etl
```

## Repository Layout and File Responsibilities

### Root Files

- `README.md`  
  Short run instructions and the reset-from-scratch workflow.

- `docker-compose.yaml`  
  Defines the MySQL service and the ETL service. The ETL service runs `run_etl.py` in a container so the full migration can be executed from one command.

- `Dockerfile`  
  Builds the Python ETL container used by Docker Compose.

- `run_etl.py`  
  Entry point for the pipeline. It simply calls the CLI runner in `etl_pipeline/cli.py`.

- `requirements.txt`  
  Python dependencies needed by the ETL code.

### `etl_pipeline/` Package

- `etl_pipeline/__init__.py`  
  Package marker.

- `etl_pipeline/config.py`  
  Defines the source MongoDB collection paths, the target table column layouts, and which tables are root tables versus child tables.

- `etl_pipeline/extract.py`  
  Performs BSON to NDJSON extraction directly in Python. This is the first stage of the pipeline.

- `etl_pipeline/transform.py`  
  Contains the document-to-table mapping logic. It reads NDJSON and converts each MongoDB collection into rows for one or more SQL tables.

- `etl_pipeline/load.py`  
  Contains the SQL schema definitions, foreign-key rules, CSV loading logic, and reject-row filtering logic.

- `etl_pipeline/cli.py`  
  Orchestrates the full ETL flow. It coordinates extraction, transformation, validation, and loading.

### MongoDB Source Dump Folders

These are the input files for the migration:

- `rasp_db/`  
  Main application collections such as users, applications, rounds, messages, drive, queries, withdrawals, and related entities.

- `ai_workflows/`  
  Workflow-related MongoDB collections such as checkpoints and AI model config.

- `DB_ACCOUNT/`  
  Role and permission data.

- `DB_AUDIT_LOG/`  
  Audit log collection.

- `documentManagementSystem/`  
  Document records used to populate the SQL documents table.

- `admin/`  
  System version metadata.

### Generated Output Folders

These are created by the ETL and are safe to delete when starting over:

- `.etl_work/`  
  Temporary NDJSON working files.

- `output/csv/`  
  Generated CSV tables produced by the transform stage.

- `output/rejects/`  
  CSV files containing rows rejected by foreign-key validation.

- `output/` database files  
  Earlier SQLite outputs that are no longer part of the final Docker-only flow.

## How Each Stage Works

### 1. Extraction

The source MongoDB data is stored as BSON files. The extractor reads each BSON file and writes one NDJSON file per collection into `.etl_work/`.

Why NDJSON first?

- It gives the transform step one document per line.
- It is easy to stream and debug.
- It avoids having to keep the entire collection in memory.

### 2. Transformation

The transform layer converts each MongoDB document into a relational row.

Examples of what it does:

- Converts `_id` into `mongo_id`.
- Renames fields like `user_id` into `user_mongo_id`.
- Converts arrays into child tables such as:
  - `document_tags`
  - `message_attachments`
  - `round_program_seats`
- Extracts common timestamps and normalizes them.
- Handles alternate field names because MongoDB documents are not always consistent across collections.

This stage produces one CSV file per table in `output/csv/`.

### 3. Foreign-Key Validation

The loader knows which columns are meant to reference other tables. For example:

- `applications.user_mongo_id -> users.mongo_id`
- `offer_history.application_mongo_id -> applications.mongo_id`
- `messages.from_mongo_id -> users.mongo_id`
- `message_attachments.file_mongo_id -> drive.mongo_id`

Before the load begins, the pipeline checks whether each child row points to an existing parent row.

If a row is invalid:

- it is removed from the data that will be loaded
- it is written to `output/rejects/<table>.csv`
- the reject file includes a reason column so someone can review it later

This is what allows the final MySQL load to keep true foreign keys enabled while still completing successfully.

### 4. Loading into MySQL

After validation, the pipeline creates the MySQL tables and inserts the cleaned CSV data.

Important details:

- Tables are created in dependency order.
- Foreign-key constraints are enabled in MySQL.
- Root tables such as `users` are loaded before dependent tables such as `applications`.
- The database is started by Docker Compose and the ETL container connects to it through the Docker network.

## Full End-to-End Flow

The exact operational flow is:

1. The user runs `docker compose up -d mysql`.
2. Docker starts the MySQL container and waits for it to become healthy.
3. The user runs `docker compose run --rm etl`.
4. The ETL container starts `run_etl.py`.
5. `run_etl.py` calls the CLI runner in `etl_pipeline/cli.py`.
6. `etl_pipeline/extract.py` reads the BSON dump folders and creates NDJSON files.
7. `etl_pipeline/transform.py` converts the NDJSON documents into CSV rows.
8. `etl_pipeline/load.py` validates FK relationships and writes rejects to `output/rejects/`.
9. `etl_pipeline/load.py` creates the MySQL schema and loads the valid rows.
10. The user opens a MySQL shell with:

```bash
docker exec -it admissions-mysql mysql -u admissions -padmissions admissions
```

11. The user can query the relational tables with normal SQL.

## Why the Reject Folder Exists

The source MongoDB data contains some records whose references do not match an existing parent record.

If those rows were inserted directly with MySQL foreign keys enabled, the load would fail. Instead, the pipeline removes them from the load set and writes them to reject CSV files.

This gives two benefits:

- The MySQL database remains relational and consistent.
- Future reviewers can inspect exactly which source rows were excluded and why.

## What Future Developers Should Know

- The MongoDB dumps are the source of truth.
- The ETL is intentionally Docker-first.
- `run_etl.py` is the one entrypoint for the full migration.
- `etl_pipeline/cli.py` is the main orchestration layer.
- `etl_pipeline/transform.py` is where field mappings are maintained.
- `etl_pipeline/load.py` is where schema, FK rules, and reject handling live.
- If the source MongoDB schema changes, updates usually belong in `transform.py` first, then `load.py` if a SQL schema change is needed.
- If a new collection is added, it must be registered in `config.py`, transformed in `transform.py`, and added to the load schema in `load.py`.

## Safe Cleanup When Starting Over

To rebuild everything from scratch:

```bash
docker compose down -v --remove-orphans
```

```powershell
if (docker ps -aq -f name=^/admissions-mysql$) { docker rm -f admissions-mysql | Out-Null }
Remove-Item -Recurse -Force .etl_work, output\csv, output\rejects -ErrorAction SilentlyContinue
```

Then rerun the migration with Docker Compose.
