CREATE TABLE reading_resource (
    id VARCHAR(255) PRIMARY KEY,
    work_key VARCHAR(255),
    provider VARCHAR(255),
    reading_mode VARCHAR(255),
    FOREIGN KEY (work_key) REFERENCES works(id) ON DELETE CASCADE
);
