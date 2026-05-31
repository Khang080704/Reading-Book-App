-- DDL cho các Entity: User, AuthorDetail, Work, Edition
-- Đã bỏ qua các entity bị @Deprecated là Author và Book

-- 1. Bảng users
CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    name VARCHAR(255),
    password VARCHAR(255)
);

-- 2. Bảng author_details
CREATE TABLE author_details (
    id VARCHAR(255) PRIMARY KEY,
    ol_key VARCHAR(255),
    birth_day VARCHAR(255),
    website VARCHAR(255),
    full_name VARCHAR(255),
    bio VARCHAR(2000),
    created_at TIMESTAMP,
    last_modify TIMESTAMP
);

-- 3. Bảng works
CREATE TABLE works (
    id VARCHAR(255) PRIMARY KEY,
    work_key VARCHAR(255) UNIQUE,
    title TEXT,
    description TEXT,
    cover_id VARCHAR(255)
);

-- 4. Bảng editions
CREATE TABLE editions (
    id VARCHAR(255) PRIMARY KEY,
    edition_key VARCHAR(255) UNIQUE,
    isbn VARCHAR(255),
    number_of_pages INT,
    publish_date VARCHAR(255),
    publisher_name VARCHAR(255),
    work_id VARCHAR(255),
    FOREIGN KEY (work_id) REFERENCES works(id) ON DELETE CASCADE
);

-- 5. Bảng work_authors (Many-to-Many giữa Work và AuthorDetail)
CREATE TABLE work_authors (
    work_id VARCHAR(255) NOT NULL,
    author_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (work_id, author_id),
    FOREIGN KEY (work_id) REFERENCES works(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author_details(id) ON DELETE CASCADE
);

-- 6. Bảng user_favorite_works (Many-to-Many giữa User và Work)
CREATE TABLE user_favorite_works (
    user_id VARCHAR(255) NOT NULL,
    work_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, work_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (work_id) REFERENCES works(id) ON DELETE CASCADE
);

-- 7. Bảng user_favorite_authors (Many-to-Many giữa User và AuthorDetail)
CREATE TABLE user_favorite_authors (
    user_id VARCHAR(255) NOT NULL,
    author_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, author_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author_details(id) ON DELETE CASCADE
);

-- ====================================================================
-- DML: Insert dữ liệu mẫu (Harry Potter, A Song of Ice and Fire, The Lord of the Rings)
-- ====================================================================

-- Insert Authors
INSERT INTO author_details (id, ol_key, birth_day, website, full_name, bio, created_at, last_modify) VALUES
('author-jk-rowling', 'OL23919A', '31 July 1965', 'jkrowling.com', 'J.K. Rowling', 'British author and philanthropist. She wrote Harry Potter, a seven-volume children''s fantasy series published from 1997 to 2007.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('author-grr-martin', 'OL234664A', 'September 20, 1948', 'georgerrmartin.com', 'George R.R. Martin', 'American author of fantasy, horror, and science fiction. Best known for A Song of Ice and Fire.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('author-jrr-tolkien', 'OL26320A', '3 January 1892', 'tolkienestate.com', 'J.R.R. Tolkien', 'English writer, poet, philologist, and academic, best known as the author of the classic high fantasy works The Hobbit and The Lord of the Rings.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Works
INSERT INTO works (id, work_key, title, description, cover_id) VALUES
('work-hp-1', 'OL82563W', 'Harry Potter and the Philosopher''s Stone', 'The first novel in the Harry Potter series and Rowling''s debut novel.', '10521270'),
('work-hp-2', 'OL82586W', 'Harry Potter and the Chamber of Secrets', 'The second novel in the Harry Potter series.', '10521271'),
('work-hp-3', 'OL82564W', 'Harry Potter and the Prisoner of Azkaban', 'The third novel in the Harry Potter series.', '10521272'),
('work-asoiaf-1', 'OL2769115W', 'A Game of Thrones', 'The first novel in A Song of Ice and Fire, a series of fantasy novels by the American author George R. R. Martin.', '8259431'),
('work-asoiaf-2', 'OL2735118W', 'A Clash of Kings', 'The second novel in A Song of Ice and Fire.', '8259432'),
('work-lotr-1', 'OL27448W', 'The Fellowship of the Ring', 'The first of three volumes of the epic novel The Lord of the Rings by the English author J. R. R. Tolkien.', '10356771'),
('work-lotr-2', 'OL27451W', 'The Two Towers', 'The second volume of The Lord of the Rings.', '10356772'),
('work-lotr-3', 'OL27454W', 'The Return of the King', 'The third and final volume of The Lord of the Rings.', '10356773');

-- Insert Work-Authors Relationships
INSERT INTO work_authors (work_id, author_id) VALUES
('work-hp-1', 'author-jk-rowling'),
('work-hp-2', 'author-jk-rowling'),
('work-hp-3', 'author-jk-rowling'),
('work-asoiaf-1', 'author-grr-martin'),
('work-asoiaf-2', 'author-grr-martin'),
('work-lotr-1', 'author-jrr-tolkien'),
('work-lotr-2', 'author-jrr-tolkien'),
('work-lotr-3', 'author-jrr-tolkien');

-- Insert Editions
INSERT INTO editions (id, edition_key, isbn, number_of_pages, publish_date, publisher_name, work_id) VALUES
('edition-hp-1', 'OL7353617M', '0747532699', 223, '1997', 'Bloomsbury', 'work-hp-1'),
('edition-hp-2', 'OL7353618M', '0747538492', 251, '1998', 'Bloomsbury', 'work-hp-2'),
('edition-hp-3', 'OL7353619M', '0747542155', 317, '1999', 'Bloomsbury', 'work-hp-3'),
('edition-asoiaf-1', 'OL3323087M', '0553103547', 694, '1996', 'Bantam Spectra', 'work-asoiaf-1'),
('edition-asoiaf-2', 'OL3323088M', '0553108034', 761, '1999', 'Bantam Spectra', 'work-asoiaf-2'),
('edition-lotr-1', 'OL20172651M', '0618346252', 398, '1954', 'Allen & Unwin', 'work-lotr-1'),
('edition-lotr-2', 'OL20172652M', '0618346260', 327, '1954', 'Allen & Unwin', 'work-lotr-2'),
('edition-lotr-3', 'OL20172653M', '0618346279', 416, '1955', 'Allen & Unwin', 'work-lotr-3');

-- Insert Dummy User & User Favorites (Optional)
INSERT INTO users (id, email, name, password) VALUES
('user-1', 'demo@example.com', 'Demo User', 'password_hash_here');

INSERT INTO user_favorite_authors (user_id, author_id) VALUES
('user-1', 'author-jk-rowling'),
('user-1', 'author-jrr-tolkien');

INSERT INTO user_favorite_works (user_id, work_id) VALUES
('user-1', 'work-hp-1'),
('user-1', 'work-lotr-1');
