# Migration Flow and Architecture

## Architecture Overview

```mermaid
flowchart LR
    subgraph Source[MongoDB Source Dump]
        A1[rasp_db/*.bson]
        A2[ai_workflows/*.bson]
        A3[DB_ACCOUNT/*.bson]
        A4[DB_AUDIT_LOG/*.bson]
        A5[documentManagementSystem/*.bson]
        A6[admin/*.bson]
    end

    subgraph ETL[Docker ETL Container]
        B1[run_etl.py]
        B2[etl_pipeline/cli.py]
        B3[etl_pipeline/extract.py]
        B4[etl_pipeline/transform.py]
        B5[etl_pipeline/load.py]
    end

    subgraph Work[Generated Working Data]
        C1[.etl_work/*.ndjson]
        C2[output/csv/*.csv]
        C3[output/rejects/*.csv]
    end

    subgraph Target[MySQL]
        D1[(MySQL tables)]
    end

    Source --> B1
    B1 --> B2
    B2 --> B3
    B3 --> C1
    C1 --> B4
    B4 --> C2
    C2 --> B5
    B5 --> C3
    B5 --> D1
```

## End-to-End Flow

```mermaid
sequenceDiagram
    actor User
    participant Compose as docker compose
    participant MySQL as admissions-mysql
    participant ETL as etl container
    participant Entry as run_etl.py
    participant CLI as etl_pipeline/cli.py
    participant Extract as etl_pipeline/extract.py
    participant Transform as etl_pipeline/transform.py
    participant Load as etl_pipeline/load.py

    User->>Compose: docker compose up -d mysql
    Compose->>MySQL: start container and wait healthy
    User->>Compose: docker compose run --rm etl
    Compose->>ETL: launch ETL container
    ETL->>Entry: python run_etl.py
    Entry->>CLI: run()
    CLI->>Extract: read BSON files
    Extract-->>CLI: write NDJSON to .etl_work/
    CLI->>Transform: map documents to relational rows
    Transform-->>CLI: write CSV files to output/csv/
    CLI->>Load: validate FK rules
    Load-->>CLI: write invalid rows to output/rejects/
    Load->>MySQL: create tables and insert valid rows
    User->>MySQL: docker exec mysql shell
```

## Table Relationship View

```mermaid
erDiagram
    USERS ||--o{ APPLICATIONS : owns
    USERS ||--o{ MESSAGES : sends_receives
    USERS ||--o{ DRIVE : owns
    USERS ||--o{ DOCUMENTS : uploads
    USERS ||--o{ QUERIES : creates
    USERS ||--o{ WITHDRAWALS : requests
    USERS ||--o{ AUDIT_LOG : triggers

    APPLICATIONS ||--o{ OFFER_HISTORY : has
    ROUNDS ||--o{ ROUND_PROGRAM_SEATS : contains
    ROUNDS ||--o{ ROUND_ALLOCATIONS : includes
    APPLICATIONS ||--o{ ROUND_ALLOCATIONS : receives
    APPLICATIONS ||--o{ ROUND_PAYMENT_RECEIPT_LOGS : pays

    MESSAGES ||--o{ MESSAGE_ATTACHMENTS : has
    DRIVE ||--o{ MESSAGE_ATTACHMENTS : stores
    DOCUMENTS ||--o{ DOCUMENT_TAGS : has
```

## Notes

- The source folders on the left are the MongoDB dump files.
- The ETL container converts them into relational rows and loads them into MySQL.
- Invalid foreign-key rows are not loaded; they are written to `output/rejects/` for review.
- The diagram reflects the current Docker-first migration setup in this repository.
