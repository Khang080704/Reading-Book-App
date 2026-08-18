/*
    This is the initial schema for the database.
*/

CREATE TABLE user_favorite_authors
(
    author_id VARCHAR(255) NOT NULL,
    user_id   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_favorite_authors PRIMARY KEY (author_id, user_id)
);

CREATE TABLE user_favorite_works
(
    user_id VARCHAR(255) NOT NULL,
    work_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_favorite_works PRIMARY KEY (user_id, work_id)
);

CREATE TABLE users
(
    id          VARCHAR(255) NOT NULL,
    email       VARCHAR(255),
    name        VARCHAR(255),
    password    VARCHAR(255),
    keycloak_id VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE author_details
(
    id                VARCHAR(255) NOT NULL,
    ol_key            VARCHAR(255),
    birth_day         VARCHAR(255),
    website           VARCHAR(255),
    full_name         VARCHAR(255),
    bio               VARCHAR(2000),
    resource_provider VARCHAR(255),
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    last_modify       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_author_details PRIMARY KEY (id)
);

CREATE TABLE work_authors
(
    author_id VARCHAR(255) NOT NULL,
    work_id   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_work_authors PRIMARY KEY (author_id, work_id)
);

CREATE TABLE works
(
    id                VARCHAR(255) NOT NULL,
    work_key          VARCHAR(255),
    title             TEXT,
    description       TEXT,
    cover_id          VARCHAR(255),
    resource_provider VARCHAR(255),
    CONSTRAINT pk_works PRIMARY KEY (id)
);

CREATE TABLE editions
(
    id              VARCHAR(255) NOT NULL,
    edition_key     VARCHAR(255),
    isbn            VARCHAR(255),
    number_of_pages INTEGER,
    publish_date    VARCHAR(255),
    publisher_name  VARCHAR(255),
    work_id         VARCHAR(255),
    CONSTRAINT pk_editions PRIMARY KEY (id)
);

CREATE TABLE chapters
(
    id          VARCHAR(255) NOT NULL,
    title       VARCHAR(255),
    resource_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_chapters PRIMARY KEY (id)
);

CREATE TABLE reading_resource
(
    id           VARCHAR(255) NOT NULL,
    work_key     VARCHAR(255),
    provider     VARCHAR(255),
    reading_mode VARCHAR(255),
    CONSTRAINT pk_reading_resource PRIMARY KEY (id)
);

CREATE TABLE author_work_sync
(
    id           VARCHAR(255) NOT NULL,
    has_next     BOOLEAN,
    next_offset  INTEGER      NOT NULL,
    batch_size   INTEGER      NOT NULL,
    last_sync_at TIMESTAMP WITHOUT TIME ZONE,
    author_id    VARCHAR(255) NOT NULL,
    total_worl   BIGINT,
    CONSTRAINT pk_author_work_sync PRIMARY KEY (id)
);



ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_keycloak UNIQUE (keycloak_id);

ALTER TABLE user_favorite_authors
    ADD CONSTRAINT fk_usefavaut_on_author_detail FOREIGN KEY (author_id) REFERENCES author_details (id);

ALTER TABLE user_favorite_authors
    ADD CONSTRAINT fk_usefavaut_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_favorite_works
    ADD CONSTRAINT fk_usefavwor_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_favorite_works
    ADD CONSTRAINT fk_usefavwor_on_work FOREIGN KEY (work_id) REFERENCES works (id);


ALTER TABLE author_details
    ADD CONSTRAINT uc_author_details_olkey UNIQUE (ol_key);


ALTER TABLE works
    ADD CONSTRAINT uc_works_workkey UNIQUE (work_key);

ALTER TABLE work_authors
    ADD CONSTRAINT fk_woraut_on_author_detail FOREIGN KEY (author_id) REFERENCES author_details (id);

ALTER TABLE work_authors
    ADD CONSTRAINT fk_woraut_on_work FOREIGN KEY (work_id) REFERENCES works (id);


ALTER TABLE editions
    ADD CONSTRAINT uc_editions_editionkey UNIQUE (edition_key);

ALTER TABLE editions
    ADD CONSTRAINT FK_EDITIONS_ON_WORK FOREIGN KEY (work_id) REFERENCES works (id);


ALTER TABLE chapters
    ADD CONSTRAINT FK_CHAPTERS_ON_RESOURCE FOREIGN KEY (resource_id) REFERENCES reading_resource (id);


ALTER TABLE reading_resource
    ADD CONSTRAINT FK_READING_RESOURCE_ON_WORK_KEY FOREIGN KEY (work_key) REFERENCES works (id);


ALTER TABLE author_work_sync
    ADD CONSTRAINT uc_author_work_sync_author UNIQUE (author_id);

ALTER TABLE author_work_sync
    ADD CONSTRAINT FK_AUTHOR_WORK_SYNC_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES author_details (id);

