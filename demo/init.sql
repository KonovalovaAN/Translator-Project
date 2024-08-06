-- init.sql
CREATE TABLE translation_requests (
    id SERIAL PRIMARY KEY,
    ip_address VARCHAR(45) NOT NULL,
    input_text TEXT NOT NULL,
    translated_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
