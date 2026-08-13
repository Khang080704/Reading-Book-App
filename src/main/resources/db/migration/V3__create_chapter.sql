CREATE TABLE chapters (
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    resource_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (resource_id) REFERENCES reading_resource(id) ON DELETE CASCADE
);
