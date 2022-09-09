CREATE TABLE IF NOT EXISTS account
(
    id         SERIAL,
    name       TEXT,
    surname    TEXT,
    create_date TIMESTAMP,
    last_update TIMESTAMP,
    balance    FLOAT
);
