CREATE TABLE IF NOT EXISTS hits
(
    hit_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR(32) NOT NULL,
    uri VARCHAR(128) NOT NULL,
    ip  VARCHAR(16) NOT NULL,
    timepoint timestamp  without time zone NOT NULL
);