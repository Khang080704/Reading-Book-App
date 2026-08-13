-- Seed reading_resource
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES
('rr-got', (SELECT id FROM works WHERE work_key = 'OL257943W'), 'OPENLIBRARY', 'CHAPTER'),
('rr-woiaf', (SELECT id FROM works WHERE work_key = 'OL17463087W'), 'OPENLIBRARY', 'CHAPTER'),
('rr-hp1', (SELECT id FROM works WHERE work_key = 'OL45832887W'), 'OPENLIBRARY', 'CHAPTER'),
('rr-darkness', (SELECT id FROM works WHERE work_key = 'OL15188323W'), 'OPENLIBRARY', 'CONTINOUS'),
('rr-fantastic', (SELECT id FROM works WHERE work_key = 'OL20126884W'), 'OPENLIBRARY', 'CONTINOUS'),
('rr-plantagenets', (SELECT id FROM works WHERE work_key = 'OL32388244W'), 'OPENLIBRARY', 'CONTINOUS'),
('rr-warsroses', (SELECT id FROM works WHERE work_key = 'OL19667570W'), 'OPENLIBRARY', 'CONTINOUS'),
('rr-afterfeast', (SELECT id FROM works WHERE work_key = 'OL19800110W'), 'OPENLIBRARY', 'CONTINOUS');

-- Seed chapters for A Game of Thrones
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-got-1', 'Prologue', 'rr-got'),
('ch-got-2', 'Bran I', 'rr-got'),
('ch-got-3', 'Catelyn I', 'rr-got'),
('ch-got-4', 'Daenerys I', 'rr-got'),
('ch-got-5', 'Eddard I', 'rr-got');

-- Seed chapters for The World of Ice & Fire
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-woiaf-1', 'Ancient History', 'rr-woiaf'),
('ch-woiaf-2', 'The Reign of the Dragons', 'rr-woiaf'),
('ch-woiaf-3', 'The Targaryen Kings', 'rr-woiaf'),
('ch-woiaf-4', 'The Fall of the Dragons', 'rr-woiaf');

-- Seed chapters for Harry Potter and the Philosopher's Stone
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-hp1-1', 'Chapter 1: The Boy Who Lived', 'rr-hp1'),
('ch-hp1-2', 'Chapter 2: The Vanishing Glass', 'rr-hp1'),
('ch-hp1-3', 'Chapter 3: The Letters from No One', 'rr-hp1'),
('ch-hp1-4', 'Chapter 4: The Keeper of the Keys', 'rr-hp1'),
('ch-hp1-5', 'Chapter 5: Diagon Alley', 'rr-hp1');
