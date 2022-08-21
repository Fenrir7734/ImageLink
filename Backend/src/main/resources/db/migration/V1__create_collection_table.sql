CREATE TABLE IF NOT EXISTS collection
(
    id          BIGSERIAL PRIMARY KEY,
    code         VARCHAR(11)   NOT NULL,
    life_period BIGINT       NOT NULL,
    title       VARCHAR(256) NOT NULL,
    description VARCHAR(2048),
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,

    CONSTRAINT uq_collection_url UNIQUE (code)
);