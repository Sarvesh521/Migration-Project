# MongoDB to MySQL Migration

This project migrates MongoDB dump data into MySQL using Docker only.

## Start From Scratch

Run these commands to remove the existing project containers, drop the MySQL volume, and clear generated ETL outputs:

```bash
docker compose down -v --remove-orphans
```

```powershell
if (docker ps -aq -f name=^/admissions-mysql$) { docker rm -f admissions-mysql | Out-Null }
Remove-Item -Recurse -Force .etl_work, output\csv, output\rejects -ErrorAction SilentlyContinue
```

If you only want to remove this project’s container and data, the commands above are enough. They do not touch unrelated Docker containers.

## Run The Migration

1. Start MySQL:
```bash
docker compose up -d mysql
```

2. Run the full MongoDB-to-MySQL ETL:
```bash
docker compose run --rm etl
```

3. Open a MySQL shell:
```bash
docker exec -it admissions-mysql mysql -u admissions -padmissions admissions
```

4. Inspect the tables inside MySQL:
```sql
SHOW TABLES;
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM applications;
SELECT COUNT(*) FROM messages;
```

## What The Script Does

- Reads the MongoDB dump files in this repository.
- Extracts BSON to temporary NDJSON.
- Transforms MongoDB documents into relational CSV tables.
- Filters rows that would break foreign-key constraints and writes them to `output/rejects/`.
- Loads the valid rows into MySQL with foreign keys enabled.

## Notes

- `output/csv/`, `output/rejects/`, and `.etl_work/` are generated and can be deleted safely.
- The ETL container runs `run_etl.py` directly and reads the MongoDB dump files from the repository through a bind mount.
