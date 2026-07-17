CREATE TABLE IF NOT EXISTS guides (
    id                  BIGSERIAL PRIMARY KEY,
    guide_number        VARCHAR(255) NOT NULL,
    carrier             VARCHAR(255) NOT NULL,
    date                DATE         NOT NULL,
    recipient           VARCHAR(255) NOT NULL,
    destination_address VARCHAR(255) NOT NULL,
    cargo_description   VARCHAR(255) NOT NULL,
    status              VARCHAR(20)  NOT NULL
        CHECK (status IN ('PENDING', 'UPLOADED', 'DELIVERED', 'DELETED')),
    efs_path            VARCHAR(255),
    s3_key              VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS processed_guides (
    id                  BIGSERIAL PRIMARY KEY,
    guide_number        VARCHAR(255) NOT NULL,
    carrier             VARCHAR(255) NOT NULL,
    date                DATE         NOT NULL,
    recipient           VARCHAR(255) NOT NULL,
    destination_address VARCHAR(255) NOT NULL,
    cargo_description   VARCHAR(255) NOT NULL,
    status              VARCHAR(20)  NOT NULL
        CHECK (status IN ('PENDING', 'UPLOADED', 'DELIVERED', 'DELETED')),
    processed_at        TIMESTAMP    NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_guides_carrier_date ON guides (carrier, date);
