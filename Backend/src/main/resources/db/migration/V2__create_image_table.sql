CREATE TABLE IF NOT EXISTS image
(
    id            BIGSERIAL    PRIMARY KEY,
    code           VARCHAR(8)   NOT NULL,
    original_url  TEXT         NOT NULL,
    title         VARCHAR(254) NOT NULL,
    description   VARCHAR(2048),
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP    NOT NULL,
    collection_id BIGINT       NOT NULL,

    CONSTRAINT uq_image_url UNIQUE (code),

    CONSTRAINT fk_image_collection
        FOREIGN KEY (collection_id)
            REFERENCES collection (id)
            ON DELETE CASCADE
);