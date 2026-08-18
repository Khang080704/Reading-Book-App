/*
    V2: Seeding Data
    - 3 Authors: J. K. Rowling, George R. R. Martin, J.R.R. Tolkien
    - 15 Works: 7 Harry Potter, 3 Lord of the Rings, 5 A Song of Ice and Fire
    - 15 Reading Resources (1 per work)
    - Full chapter listings for all works
*/

-- =============================================
-- 1. AUTHOR DETAILS
-- =============================================
INSERT INTO author_details (id, ol_key, birth_day, website, full_name, bio, resource_provider, created_at, last_modify)
VALUES
('author-001', 'OL23919A', '1965-07-31', 'https://www.jkrowling.com', 'J. K. Rowling',
 'Joanne Rowling, better known by her pen name J. K. Rowling, is a British author and philanthropist. She is best known for writing the Harry Potter fantasy series, which has won multiple awards and sold more than 500 million copies worldwide, becoming the best-selling book series in history.',
 'OPEN_LIBRARY', NOW(), NOW()),

('author-002', 'OL234664A', '1948-09-20', 'https://georgerrmartin.com', 'George R. R. Martin',
 'George Raymond Richard Martin, also known as GRRM, is an American novelist, screenwriter, television producer and short story writer. He is the author of the series of epic fantasy novels A Song of Ice and Fire, which was adapted into the Emmy Award-winning HBO series Game of Thrones.',
 'OPEN_LIBRARY', NOW(), NOW()),

('author-003', 'OL26320A', '1892-01-03', NULL, 'J.R.R. Tolkien',
 'John Ronald Reuel Tolkien was an English writer, poet, philologist, and academic, best known as the author of the high fantasy works The Hobbit and The Lord of the Rings. He served as the Rawlinson and Bosworth Professor of Anglo-Saxon and Fellow of Pembroke College, Oxford, from 1925 to 1945.',
 'OPEN_LIBRARY', NOW(), NOW());


-- =============================================
-- 2. WORKS
-- =============================================

-- George R. R. Martin - A Song of Ice and Fire
INSERT INTO works (id, work_key, title, description, cover_id, resource_provider)
VALUES
('work-001', 'OL257943W', 'A Game of Thrones',
 'The first novel in A Song of Ice and Fire, an epic fantasy series. Set in the fictional continents of Westeros and Essos, the story follows multiple storylines: the Stark family drawn into dangerous court politics, the exiled Targaryen siblings plotting to reclaim the throne, and the Night''s Watch defending the Wall against ancient threats from the north.',
 NULL, 'INTERNAL'),

('work-002', 'OL257939W', 'A Clash of Kings',
 'The second novel in A Song of Ice and Fire. With King Robert dead, several claimants vie for the Iron Throne in a devastating civil war known as the War of the Five Kings. Meanwhile, a comet blazes across the sky, heralding a time of blood and turmoil.',
 NULL, 'INTERNAL'),

('work-003', 'OL257914W', 'A Storm of Swords',
 'The third novel in A Song of Ice and Fire. The War of the Five Kings continues as alliances crumble, shocking betrayals unfold, and the fate of the Seven Kingdoms hangs in the balance. Features some of the most pivotal events in the entire saga.',
 NULL, 'INTERNAL'),

('work-004', 'OL257948W', 'A Feast for Crows',
 'The fourth novel in A Song of Ice and Fire. As the war ravages Westeros, the survivors struggle to rebuild in a fractured kingdom. New players emerge in the game of thrones while old powers seek to consolidate their hold on the realm.',
 NULL, 'INTERNAL'),

('work-005', 'OL1955906W', 'A Dance with Dragons',
 'The fifth novel in A Song of Ice and Fire. Jon Snow faces mounting challenges as Lord Commander of the Night''s Watch, Daenerys Targaryen struggles to rule the city of Meereen, and Tyrion Lannister embarks on a journey across Essos that will change his destiny.',
 NULL, 'INTERNAL');

-- J.R.R. Tolkien - The Lord of the Rings
INSERT INTO works (id, work_key, title, description, cover_id, resource_provider)
VALUES
('work-006', 'OL27513W', 'The Fellowship of the Ring',
 'The first volume of The Lord of the Rings. Young hobbit Frodo Baggins inherits the One Ring from his uncle Bilbo and must undertake a perilous journey to the Cracks of Doom to destroy it. Accompanied by the Fellowship — four hobbits, an elf, a dwarf, two men, and a wizard — he sets out from the Shire on a quest that will determine the fate of Middle-earth.',
 NULL, 'INTERNAL'),

('work-007', 'OL27479W', 'The Two Towers',
 'The second volume of The Lord of the Rings. The Fellowship has been broken. While Frodo and Sam continue their journey to Mordor with the treacherous Gollum as their guide, Aragorn, Legolas, and Gimli pursue the Uruk-hai who have captured Merry and Pippin. War comes to Rohan and the ancient forests of Fangorn stir.',
 NULL, 'INTERNAL'),

('work-008', 'OL27455W', 'The Return of the King',
 'The third and final volume of The Lord of the Rings. The forces of Sauron lay siege to Minas Tirith in a climactic battle for Middle-earth, while Frodo and Sam struggle through the desolate land of Mordor toward Mount Doom, bearing the burden of the One Ring.',
 NULL, 'INTERNAL');

-- J. K. Rowling - Harry Potter
INSERT INTO works (id, work_key, title, description, cover_id, resource_provider)
VALUES
('work-009', 'OL82563W', 'Harry Potter and the Sorcerer''s Stone',
 'The first novel in the Harry Potter series. Harry Potter, an orphan raised by his cruel aunt and uncle, discovers on his eleventh birthday that he is a wizard. He enters Hogwarts School of Witchcraft and Wizardry, makes friends and enemies, and uncovers the mystery of the Sorcerer''s Stone.',
 NULL, 'INTERNAL'),

('work-010', 'OL82537W', 'Harry Potter and the Chamber of Secrets',
 'The second novel in the Harry Potter series. Harry returns to Hogwarts after a miserable summer and discovers that a mysterious force is petrifying students. With the help of his friends, he must uncover the secret of the Chamber of Secrets before it is too late.',
 NULL, 'INTERNAL'),

('work-011', 'OL82536W', 'Harry Potter and the Prisoner of Azkaban',
 'The third novel in the Harry Potter series. Harry learns that Sirius Black, a convicted murderer who allegedly betrayed his parents, has escaped from the wizard prison Azkaban. As Dementors patrol the school grounds, Harry uncovers surprising truths about his past.',
 NULL, 'INTERNAL'),

('work-012', 'OL82560W', 'Harry Potter and the Goblet of Fire',
 'The fourth novel in the Harry Potter series. Harry is unexpectedly entered into the Triwizard Tournament, a dangerous magical competition between three wizarding schools. As he faces each deadly task, a darker plot unfolds that will lead to the return of Lord Voldemort.',
 NULL, 'INTERNAL'),

('work-013', 'OL82548W', 'Harry Potter and the Order of the Phoenix',
 'The fifth novel in the Harry Potter series. With the Ministry of Magic in denial about Voldemort''s return, Harry and his friends form Dumbledore''s Army to prepare for the coming war. Meanwhile, Harry discovers a mysterious connection to the Dark Lord.',
 NULL, 'INTERNAL'),

('work-014', 'OL82565W', 'Harry Potter and the Half-Blood Prince',
 'The sixth novel in the Harry Potter series. Harry delves into the past of Lord Voldemort with Dumbledore, discovering the secret of Horcruxes. Meanwhile, a mysterious textbook belonging to the Half-Blood Prince transforms Harry''s potions studies.',
 NULL, 'INTERNAL'),

('work-015', 'OL82586W', 'Harry Potter and the Deathly Hallows',
 'The seventh and final novel in the Harry Potter series. Harry, Ron, and Hermione abandon their seventh year at Hogwarts to hunt down and destroy Voldemort''s remaining Horcruxes. The quest leads them to discover the legend of the Deathly Hallows and culminates in the Battle of Hogwarts.',
 NULL, 'INTERNAL');


-- =============================================
-- 3. WORK AUTHORS
-- =============================================

-- George R. R. Martin's works
INSERT INTO work_authors (author_id, work_id) VALUES ('author-002', 'work-001');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-002', 'work-002');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-002', 'work-003');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-002', 'work-004');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-002', 'work-005');

-- J.R.R. Tolkien's works
INSERT INTO work_authors (author_id, work_id) VALUES ('author-003', 'work-006');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-003', 'work-007');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-003', 'work-008');

-- J. K. Rowling's works
INSERT INTO work_authors (author_id, work_id) VALUES ('author-001', 'work-009');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-001', 'work-010');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-001', 'work-011');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-001', 'work-012');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-001', 'work-013');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-001', 'work-014');
INSERT INTO work_authors (author_id, work_id) VALUES ('author-001', 'work-015');


-- =============================================
-- 4. READING RESOURCES (1 per work)
--    work_key references works.id (NOT works.work_key)
--    provider = 'INTERNAL', reading_mode = 'CHAPTER'
-- =============================================
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-001', 'work-001', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-002', 'work-002', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-003', 'work-003', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-004', 'work-004', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-005', 'work-005', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-006', 'work-006', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-007', 'work-007', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-008', 'work-008', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-009', 'work-009', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-010', 'work-010', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-011', 'work-011', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-012', 'work-012', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-013', 'work-013', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-014', 'work-014', 'INTERNAL', 'CHAPTER');
INSERT INTO reading_resource (id, work_key, provider, reading_mode) VALUES ('rr-015', 'work-015', 'INTERNAL', 'CHAPTER');


-- =============================================
-- 5. CHAPTERS
-- =============================================

-- ===========================================
-- A Game of Thrones (work-001, rr-001) — 73 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-001-001', 'Prologue', 'rr-001'),
('ch-001-002', 'Bran I', 'rr-001'),
('ch-001-003', 'Catelyn I', 'rr-001'),
('ch-001-004', 'Daenerys I', 'rr-001'),
('ch-001-005', 'Eddard I', 'rr-001'),
('ch-001-006', 'Jon I', 'rr-001'),
('ch-001-007', 'Catelyn II', 'rr-001'),
('ch-001-008', 'Arya I', 'rr-001'),
('ch-001-009', 'Bran II', 'rr-001'),
('ch-001-010', 'Tyrion I', 'rr-001'),
('ch-001-011', 'Jon II', 'rr-001'),
('ch-001-012', 'Daenerys II', 'rr-001'),
('ch-001-013', 'Eddard II', 'rr-001'),
('ch-001-014', 'Tyrion II', 'rr-001'),
('ch-001-015', 'Catelyn III', 'rr-001'),
('ch-001-016', 'Sansa I', 'rr-001'),
('ch-001-017', 'Eddard III', 'rr-001'),
('ch-001-018', 'Bran III', 'rr-001'),
('ch-001-019', 'Catelyn IV', 'rr-001'),
('ch-001-020', 'Jon III', 'rr-001'),
('ch-001-021', 'Eddard IV', 'rr-001'),
('ch-001-022', 'Tyrion III', 'rr-001'),
('ch-001-023', 'Arya II', 'rr-001'),
('ch-001-024', 'Daenerys III', 'rr-001'),
('ch-001-025', 'Bran IV', 'rr-001'),
('ch-001-026', 'Eddard V', 'rr-001'),
('ch-001-027', 'Jon IV', 'rr-001'),
('ch-001-028', 'Eddard VI', 'rr-001'),
('ch-001-029', 'Sansa II', 'rr-001'),
('ch-001-030', 'Eddard VII', 'rr-001'),
('ch-001-031', 'Tyrion IV', 'rr-001'),
('ch-001-032', 'Arya III', 'rr-001'),
('ch-001-033', 'Eddard VIII', 'rr-001'),
('ch-001-034', 'Catelyn V', 'rr-001'),
('ch-001-035', 'Eddard IX', 'rr-001'),
('ch-001-036', 'Catelyn VI', 'rr-001'),
('ch-001-037', 'Eddard X', 'rr-001'),
('ch-001-038', 'Tyrion V', 'rr-001'),
('ch-001-039', 'Eddard XI', 'rr-001'),
('ch-001-040', 'Catelyn VII', 'rr-001'),
('ch-001-041', 'Jon V', 'rr-001'),
('ch-001-042', 'Tyrion VI', 'rr-001'),
('ch-001-043', 'Eddard XII', 'rr-001'),
('ch-001-044', 'Sansa III', 'rr-001'),
('ch-001-045', 'Eddard XIII', 'rr-001'),
('ch-001-046', 'Daenerys IV', 'rr-001'),
('ch-001-047', 'Eddard XIV', 'rr-001'),
('ch-001-048', 'Catelyn VIII', 'rr-001'),
('ch-001-049', 'Jon VI', 'rr-001'),
('ch-001-050', 'Eddard XV', 'rr-001'),
('ch-001-051', 'Sansa IV', 'rr-001'),
('ch-001-052', 'Daenerys V', 'rr-001'),
('ch-001-053', 'Jon VII', 'rr-001'),
('ch-001-054', 'Bran V', 'rr-001'),
('ch-001-055', 'Sansa V', 'rr-001'),
('ch-001-056', 'Catelyn IX', 'rr-001'),
('ch-001-057', 'Daenerys VI', 'rr-001'),
('ch-001-058', 'Tyrion VII', 'rr-001'),
('ch-001-059', 'Sansa VI', 'rr-001'),
('ch-001-060', 'Catelyn X', 'rr-001'),
('ch-001-061', 'Jon VIII', 'rr-001'),
('ch-001-062', 'Daenerys VII', 'rr-001'),
('ch-001-063', 'Tyrion VIII', 'rr-001'),
('ch-001-064', 'Catelyn XI', 'rr-001'),
('ch-001-065', 'Daenerys VIII', 'rr-001'),
('ch-001-066', 'Arya IV', 'rr-001'),
('ch-001-067', 'Bran VI', 'rr-001'),
('ch-001-068', 'Tyrion IX', 'rr-001'),
('ch-001-069', 'Daenerys IX', 'rr-001'),
('ch-001-070', 'Jon IX', 'rr-001'),
('ch-001-071', 'Arya V', 'rr-001'),
('ch-001-072', 'Bran VII', 'rr-001'),
('ch-001-073', 'Daenerys X', 'rr-001');


-- ===========================================
-- A Clash of Kings (work-002, rr-002) — 70 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-002-001', 'Prologue', 'rr-002'),
('ch-002-002', 'Arya I', 'rr-002'),
('ch-002-003', 'Sansa I', 'rr-002'),
('ch-002-004', 'Tyrion I', 'rr-002'),
('ch-002-005', 'Bran I', 'rr-002'),
('ch-002-006', 'Arya II', 'rr-002'),
('ch-002-007', 'Jon I', 'rr-002'),
('ch-002-008', 'Catelyn I', 'rr-002'),
('ch-002-009', 'Tyrion II', 'rr-002'),
('ch-002-010', 'Arya III', 'rr-002'),
('ch-002-011', 'Davos I', 'rr-002'),
('ch-002-012', 'Theon I', 'rr-002'),
('ch-002-013', 'Daenerys I', 'rr-002'),
('ch-002-014', 'Jon II', 'rr-002'),
('ch-002-015', 'Arya IV', 'rr-002'),
('ch-002-016', 'Tyrion III', 'rr-002'),
('ch-002-017', 'Sansa II', 'rr-002'),
('ch-002-018', 'Tyrion IV', 'rr-002'),
('ch-002-019', 'Arya V', 'rr-002'),
('ch-002-020', 'Tyrion V', 'rr-002'),
('ch-002-021', 'Bran II', 'rr-002'),
('ch-002-022', 'Catelyn II', 'rr-002'),
('ch-002-023', 'Tyrion VI', 'rr-002'),
('ch-002-024', 'Theon II', 'rr-002'),
('ch-002-025', 'Catelyn III', 'rr-002'),
('ch-002-026', 'Tyrion VII', 'rr-002'),
('ch-002-027', 'Arya VI', 'rr-002'),
('ch-002-028', 'Daenerys II', 'rr-002'),
('ch-002-029', 'Bran III', 'rr-002'),
('ch-002-030', 'Tyrion VIII', 'rr-002'),
('ch-002-031', 'Catelyn IV', 'rr-002'),
('ch-002-032', 'Sansa III', 'rr-002'),
('ch-002-033', 'Catelyn V', 'rr-002'),
('ch-002-034', 'Jon III', 'rr-002'),
('ch-002-035', 'Tyrion IX', 'rr-002'),
('ch-002-036', 'Arya VII', 'rr-002'),
('ch-002-037', 'Theon III', 'rr-002'),
('ch-002-038', 'Tyrion X', 'rr-002'),
('ch-002-039', 'Catelyn VI', 'rr-002'),
('ch-002-040', 'Daenerys III', 'rr-002'),
('ch-002-041', 'Tyrion XI', 'rr-002'),
('ch-002-042', 'Davos II', 'rr-002'),
('ch-002-043', 'Jon IV', 'rr-002'),
('ch-002-044', 'Tyrion XII', 'rr-002'),
('ch-002-045', 'Catelyn VII', 'rr-002'),
('ch-002-046', 'Bran IV', 'rr-002'),
('ch-002-047', 'Arya VIII', 'rr-002'),
('ch-002-048', 'Daenerys IV', 'rr-002'),
('ch-002-049', 'Tyrion XIII', 'rr-002'),
('ch-002-050', 'Jon V', 'rr-002'),
('ch-002-051', 'Sansa IV', 'rr-002'),
('ch-002-052', 'Theon IV', 'rr-002'),
('ch-002-053', 'Jon VI', 'rr-002'),
('ch-002-054', 'Tyrion XIV', 'rr-002'),
('ch-002-055', 'Sansa V', 'rr-002'),
('ch-002-056', 'Theon V', 'rr-002'),
('ch-002-057', 'Davos III', 'rr-002'),
('ch-002-058', 'Tyrion XV', 'rr-002'),
('ch-002-059', 'Sansa VI', 'rr-002'),
('ch-002-060', 'Jon VII', 'rr-002'),
('ch-002-061', 'Bran V', 'rr-002'),
('ch-002-062', 'Arya IX', 'rr-002'),
('ch-002-063', 'Sansa VII', 'rr-002'),
('ch-002-064', 'Theon VI', 'rr-002'),
('ch-002-065', 'Jon VIII', 'rr-002'),
('ch-002-066', 'Bran VI', 'rr-002'),
('ch-002-067', 'Arya X', 'rr-002'),
('ch-002-068', 'Daenerys V', 'rr-002'),
('ch-002-069', 'Sansa VIII', 'rr-002'),
('ch-002-070', 'Bran VII', 'rr-002');


-- ===========================================
-- A Storm of Swords (work-003, rr-003) — 82 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-003-001', 'Prologue', 'rr-003'),
('ch-003-002', 'Jaime I', 'rr-003'),
('ch-003-003', 'Catelyn I', 'rr-003'),
('ch-003-004', 'Arya I', 'rr-003'),
('ch-003-005', 'Tyrion I', 'rr-003'),
('ch-003-006', 'Davos I', 'rr-003'),
('ch-003-007', 'Sansa I', 'rr-003'),
('ch-003-008', 'Jon I', 'rr-003'),
('ch-003-009', 'Daenerys I', 'rr-003'),
('ch-003-010', 'Bran I', 'rr-003'),
('ch-003-011', 'Davos II', 'rr-003'),
('ch-003-012', 'Jaime II', 'rr-003'),
('ch-003-013', 'Tyrion II', 'rr-003'),
('ch-003-014', 'Arya II', 'rr-003'),
('ch-003-015', 'Catelyn II', 'rr-003'),
('ch-003-016', 'Jon II', 'rr-003'),
('ch-003-017', 'Sansa II', 'rr-003'),
('ch-003-018', 'Arya III', 'rr-003'),
('ch-003-019', 'Samwell I', 'rr-003'),
('ch-003-020', 'Tyrion III', 'rr-003'),
('ch-003-021', 'Catelyn III', 'rr-003'),
('ch-003-022', 'Jaime III', 'rr-003'),
('ch-003-023', 'Arya IV', 'rr-003'),
('ch-003-024', 'Daenerys II', 'rr-003'),
('ch-003-025', 'Bran II', 'rr-003'),
('ch-003-026', 'Jon III', 'rr-003'),
('ch-003-027', 'Daenerys III', 'rr-003'),
('ch-003-028', 'Sansa III', 'rr-003'),
('ch-003-029', 'Tyrion IV', 'rr-003'),
('ch-003-030', 'Arya V', 'rr-003'),
('ch-003-031', 'Jaime IV', 'rr-003'),
('ch-003-032', 'Tyrion V', 'rr-003'),
('ch-003-033', 'Catelyn IV', 'rr-003'),
('ch-003-034', 'Arya VI', 'rr-003'),
('ch-003-035', 'Samwell II', 'rr-003'),
('ch-003-036', 'Jon IV', 'rr-003'),
('ch-003-037', 'Jaime V', 'rr-003'),
('ch-003-038', 'Tyrion VI', 'rr-003'),
('ch-003-039', 'Arya VII', 'rr-003'),
('ch-003-040', 'Catelyn V', 'rr-003'),
('ch-003-041', 'Daenerys IV', 'rr-003'),
('ch-003-042', 'Jon V', 'rr-003'),
('ch-003-043', 'Tyrion VII', 'rr-003'),
('ch-003-044', 'Davos III', 'rr-003'),
('ch-003-045', 'Arya VIII', 'rr-003'),
('ch-003-046', 'Jon VI', 'rr-003'),
('ch-003-047', 'Samwell III', 'rr-003'),
('ch-003-048', 'Catelyn VI', 'rr-003'),
('ch-003-049', 'Arya IX', 'rr-003'),
('ch-003-050', 'Jaime VI', 'rr-003'),
('ch-003-051', 'Catelyn VII', 'rr-003'),
('ch-003-052', 'Tyrion VIII', 'rr-003'),
('ch-003-053', 'Davos IV', 'rr-003'),
('ch-003-054', 'Daenerys V', 'rr-003'),
('ch-003-055', 'Jon VII', 'rr-003'),
('ch-003-056', 'Arya X', 'rr-003'),
('ch-003-057', 'Bran III', 'rr-003'),
('ch-003-058', 'Sansa IV', 'rr-003'),
('ch-003-059', 'Tyrion IX', 'rr-003'),
('ch-003-060', 'Samwell IV', 'rr-003'),
('ch-003-061', 'Jon VIII', 'rr-003'),
('ch-003-062', 'Arya XI', 'rr-003'),
('ch-003-063', 'Jaime VII', 'rr-003'),
('ch-003-064', 'Tyrion X', 'rr-003'),
('ch-003-065', 'Jon IX', 'rr-003'),
('ch-003-066', 'Sansa V', 'rr-003'),
('ch-003-067', 'Daenerys VI', 'rr-003'),
('ch-003-068', 'Jaime VIII', 'rr-003'),
('ch-003-069', 'Jon X', 'rr-003'),
('ch-003-070', 'Bran IV', 'rr-003'),
('ch-003-071', 'Tyrion XI', 'rr-003'),
('ch-003-072', 'Sansa VI', 'rr-003'),
('ch-003-073', 'Davos V', 'rr-003'),
('ch-003-074', 'Jaime IX', 'rr-003'),
('ch-003-075', 'Jon XI', 'rr-003'),
('ch-003-076', 'Samwell V', 'rr-003'),
('ch-003-077', 'Sansa VII', 'rr-003'),
('ch-003-078', 'Davos VI', 'rr-003'),
('ch-003-079', 'Jon XII', 'rr-003'),
('ch-003-080', 'Arya XII', 'rr-003'),
('ch-003-081', 'Arya XIII', 'rr-003'),
('ch-003-082', 'Epilogue', 'rr-003');


-- ===========================================
-- A Feast for Crows (work-004, rr-004) — 46 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-004-001', 'Prologue', 'rr-004'),
('ch-004-002', 'The Prophet', 'rr-004'),
('ch-004-003', 'The Captain of Guards', 'rr-004'),
('ch-004-004', 'Cersei I', 'rr-004'),
('ch-004-005', 'Brienne I', 'rr-004'),
('ch-004-006', 'Samwell I', 'rr-004'),
('ch-004-007', 'Arya I', 'rr-004'),
('ch-004-008', 'Cersei II', 'rr-004'),
('ch-004-009', 'Jaime I', 'rr-004'),
('ch-004-010', 'Brienne II', 'rr-004'),
('ch-004-011', 'Sansa I', 'rr-004'),
('ch-004-012', 'Cersei III', 'rr-004'),
('ch-004-013', 'The Soiled Knight', 'rr-004'),
('ch-004-014', 'Brienne III', 'rr-004'),
('ch-004-015', 'Samwell II', 'rr-004'),
('ch-004-016', 'Jaime II', 'rr-004'),
('ch-004-017', 'Cersei IV', 'rr-004'),
('ch-004-018', 'The Iron Captain', 'rr-004'),
('ch-004-019', 'The Drowned Man', 'rr-004'),
('ch-004-020', 'Brienne IV', 'rr-004'),
('ch-004-021', 'The Queenmaker', 'rr-004'),
('ch-004-022', 'Arya II', 'rr-004'),
('ch-004-023', 'Alayne I', 'rr-004'),
('ch-004-024', 'Cersei V', 'rr-004'),
('ch-004-025', 'Brienne V', 'rr-004'),
('ch-004-026', 'Samwell III', 'rr-004'),
('ch-004-027', 'Jaime III', 'rr-004'),
('ch-004-028', 'Cersei VI', 'rr-004'),
('ch-004-029', 'The Reaver', 'rr-004'),
('ch-004-030', 'Jaime IV', 'rr-004'),
('ch-004-031', 'Brienne VI', 'rr-004'),
('ch-004-032', 'Cersei VII', 'rr-004'),
('ch-004-033', 'Jaime V', 'rr-004'),
('ch-004-034', 'Cat of the Canals', 'rr-004'),
('ch-004-035', 'Samwell IV', 'rr-004'),
('ch-004-036', 'Cersei VIII', 'rr-004'),
('ch-004-037', 'Brienne VII', 'rr-004'),
('ch-004-038', 'Jaime VI', 'rr-004'),
('ch-004-039', 'Cersei IX', 'rr-004'),
('ch-004-040', 'The Princess in the Tower', 'rr-004'),
('ch-004-041', 'Alayne II', 'rr-004'),
('ch-004-042', 'Brienne VIII', 'rr-004'),
('ch-004-043', 'Cersei X', 'rr-004'),
('ch-004-044', 'Jaime VII', 'rr-004'),
('ch-004-045', 'Samwell V', 'rr-004'),
('ch-004-046', 'The Blind Girl', 'rr-004');


-- ===========================================
-- A Dance with Dragons (work-005, rr-005) — 73 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-005-001', 'Prologue', 'rr-005'),
('ch-005-002', 'Tyrion I', 'rr-005'),
('ch-005-003', 'Daenerys I', 'rr-005'),
('ch-005-004', 'Jon I', 'rr-005'),
('ch-005-005', 'The Merchant''s Man', 'rr-005'),
('ch-005-006', 'Reek I', 'rr-005'),
('ch-005-007', 'Tyrion II', 'rr-005'),
('ch-005-008', 'Jon II', 'rr-005'),
('ch-005-009', 'Davos I', 'rr-005'),
('ch-005-010', 'The Lost Lord', 'rr-005'),
('ch-005-011', 'Daenerys II', 'rr-005'),
('ch-005-012', 'Jon III', 'rr-005'),
('ch-005-013', 'Reek II', 'rr-005'),
('ch-005-014', 'Bran I', 'rr-005'),
('ch-005-015', 'Tyrion III', 'rr-005'),
('ch-005-016', 'Davos II', 'rr-005'),
('ch-005-017', 'Daenerys III', 'rr-005'),
('ch-005-018', 'Jon IV', 'rr-005'),
('ch-005-019', 'Tyrion IV', 'rr-005'),
('ch-005-020', 'The Wayward Bride', 'rr-005'),
('ch-005-021', 'Jon V', 'rr-005'),
('ch-005-022', 'Tyrion V', 'rr-005'),
('ch-005-023', 'Davos III', 'rr-005'),
('ch-005-024', 'Daenerys IV', 'rr-005'),
('ch-005-025', 'The Prince of Winterfell', 'rr-005'),
('ch-005-026', 'The Windblown', 'rr-005'),
('ch-005-027', 'The Watcher', 'rr-005'),
('ch-005-028', 'Jon VI', 'rr-005'),
('ch-005-029', 'Tyrion VI', 'rr-005'),
('ch-005-030', 'Davos IV', 'rr-005'),
('ch-005-031', 'Melisandre I', 'rr-005'),
('ch-005-032', 'Reek III', 'rr-005'),
('ch-005-033', 'Tyrion VII', 'rr-005'),
('ch-005-034', 'Jon VII', 'rr-005'),
('ch-005-035', 'Bran II', 'rr-005'),
('ch-005-036', 'Daenerys V', 'rr-005'),
('ch-005-037', 'Jon VIII', 'rr-005'),
('ch-005-038', 'Tyrion VIII', 'rr-005'),
('ch-005-039', 'The Turncloak', 'rr-005'),
('ch-005-040', 'Jon IX', 'rr-005'),
('ch-005-041', 'Daenerys VI', 'rr-005'),
('ch-005-042', 'The King''s Prize', 'rr-005'),
('ch-005-043', 'Tyrion IX', 'rr-005'),
('ch-005-044', 'Jon X', 'rr-005'),
('ch-005-045', 'Daenerys VII', 'rr-005'),
('ch-005-046', 'The Iron Suitor', 'rr-005'),
('ch-005-047', 'Tyrion X', 'rr-005'),
('ch-005-048', 'Bran III', 'rr-005'),
('ch-005-049', 'Jon XI', 'rr-005'),
('ch-005-050', 'Daenerys VIII', 'rr-005'),
('ch-005-051', 'The Spurned Suitor', 'rr-005'),
('ch-005-052', 'The Griffin Reborn', 'rr-005'),
('ch-005-053', 'Tyrion XI', 'rr-005'),
('ch-005-054', 'Jon XII', 'rr-005'),
('ch-005-055', 'The Queensguard', 'rr-005'),
('ch-005-056', 'The Sacrifice', 'rr-005'),
('ch-005-057', 'Tyrion XII', 'rr-005'),
('ch-005-058', 'Jon XIII', 'rr-005'),
('ch-005-059', 'The Ugly Little Girl', 'rr-005'),
('ch-005-060', 'Cersei I', 'rr-005'),
('ch-005-061', 'The Kingbreaker', 'rr-005'),
('ch-005-062', 'The Discarded Knight', 'rr-005'),
('ch-005-063', 'Victarion I', 'rr-005'),
('ch-005-064', 'The Queen''s Hand', 'rr-005'),
('ch-005-065', 'The Dragontamer', 'rr-005'),
('ch-005-066', 'Daenerys IX', 'rr-005'),
('ch-005-067', 'Jon XIV', 'rr-005'),
('ch-005-068', 'Cersei II', 'rr-005'),
('ch-005-069', 'Daenerys X', 'rr-005'),
('ch-005-070', 'Victarion II', 'rr-005'),
('ch-005-071', 'Barristan I', 'rr-005'),
('ch-005-072', 'Barristan II', 'rr-005'),
('ch-005-073', 'Epilogue', 'rr-005');


-- ===========================================
-- The Fellowship of the Ring (work-006, rr-006) — 23 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-006-001', 'Prologue: Concerning Hobbits', 'rr-006'),
('ch-006-002', 'A Long-expected Party', 'rr-006'),
('ch-006-003', 'The Shadow of the Past', 'rr-006'),
('ch-006-004', 'Three is Company', 'rr-006'),
('ch-006-005', 'A Short Cut to Mushrooms', 'rr-006'),
('ch-006-006', 'A Conspiracy Unmasked', 'rr-006'),
('ch-006-007', 'The Old Forest', 'rr-006'),
('ch-006-008', 'In the House of Tom Bombadil', 'rr-006'),
('ch-006-009', 'Fog on the Barrow-Downs', 'rr-006'),
('ch-006-010', 'At the Sign of the Prancing Pony', 'rr-006'),
('ch-006-011', 'Strider', 'rr-006'),
('ch-006-012', 'A Knife in the Dark', 'rr-006'),
('ch-006-013', 'Flight to the Ford', 'rr-006'),
('ch-006-014', 'Many Meetings', 'rr-006'),
('ch-006-015', 'The Council of Elrond', 'rr-006'),
('ch-006-016', 'The Ring Goes South', 'rr-006'),
('ch-006-017', 'A Journey in the Dark', 'rr-006'),
('ch-006-018', 'The Bridge of Khazad-dum', 'rr-006'),
('ch-006-019', 'Lothlorien', 'rr-006'),
('ch-006-020', 'The Mirror of Galadriel', 'rr-006'),
('ch-006-021', 'Farewell to Lorien', 'rr-006'),
('ch-006-022', 'The Great River', 'rr-006'),
('ch-006-023', 'The Breaking of the Fellowship', 'rr-006');


-- ===========================================
-- The Two Towers (work-007, rr-007) — 21 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-007-001', 'The Departure of Boromir', 'rr-007'),
('ch-007-002', 'The Riders of Rohan', 'rr-007'),
('ch-007-003', 'The Uruk-hai', 'rr-007'),
('ch-007-004', 'Treebeard', 'rr-007'),
('ch-007-005', 'The White Rider', 'rr-007'),
('ch-007-006', 'The King of the Golden Hall', 'rr-007'),
('ch-007-007', 'Helm''s Deep', 'rr-007'),
('ch-007-008', 'The Road to Isengard', 'rr-007'),
('ch-007-009', 'Flotsam and Jetsam', 'rr-007'),
('ch-007-010', 'The Voice of Saruman', 'rr-007'),
('ch-007-011', 'The Palantir', 'rr-007'),
('ch-007-012', 'The Taming of Smeagol', 'rr-007'),
('ch-007-013', 'The Passage of the Marshes', 'rr-007'),
('ch-007-014', 'The Black Gate is Closed', 'rr-007'),
('ch-007-015', 'Of Herbs and Stewed Rabbit', 'rr-007'),
('ch-007-016', 'The Window on the West', 'rr-007'),
('ch-007-017', 'The Forbidden Pool', 'rr-007'),
('ch-007-018', 'Journey to the Cross-Roads', 'rr-007'),
('ch-007-019', 'The Stairs of Cirith Ungol', 'rr-007'),
('ch-007-020', 'Shelob''s Lair', 'rr-007'),
('ch-007-021', 'The Choices of Master Samwise', 'rr-007');


-- ===========================================
-- The Return of the King (work-008, rr-008) — 19 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-008-001', 'Minas Tirith', 'rr-008'),
('ch-008-002', 'The Passing of the Grey Company', 'rr-008'),
('ch-008-003', 'The Muster of Rohan', 'rr-008'),
('ch-008-004', 'The Siege of Gondor', 'rr-008'),
('ch-008-005', 'The Ride of the Rohirrim', 'rr-008'),
('ch-008-006', 'The Battle of the Pelennor Fields', 'rr-008'),
('ch-008-007', 'The Pyre of Denethor', 'rr-008'),
('ch-008-008', 'The Houses of Healing', 'rr-008'),
('ch-008-009', 'The Last Debate', 'rr-008'),
('ch-008-010', 'The Black Gate Opens', 'rr-008'),
('ch-008-011', 'The Tower of Cirith Ungol', 'rr-008'),
('ch-008-012', 'The Land of Shadow', 'rr-008'),
('ch-008-013', 'Mount Doom', 'rr-008'),
('ch-008-014', 'The Field of Cormallen', 'rr-008'),
('ch-008-015', 'The Steward and the King', 'rr-008'),
('ch-008-016', 'Many Partings', 'rr-008'),
('ch-008-017', 'Homeward Bound', 'rr-008'),
('ch-008-018', 'The Scouring of the Shire', 'rr-008'),
('ch-008-019', 'The Grey Havens', 'rr-008');


-- ===========================================
-- Harry Potter and the Sorcerer's Stone (work-009, rr-009) — 17 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-009-001', 'The Boy Who Lived', 'rr-009'),
('ch-009-002', 'The Vanishing Glass', 'rr-009'),
('ch-009-003', 'The Letters from No One', 'rr-009'),
('ch-009-004', 'The Keeper of the Keys', 'rr-009'),
('ch-009-005', 'Diagon Alley', 'rr-009'),
('ch-009-006', 'The Journey from Platform Nine and Three-Quarters', 'rr-009'),
('ch-009-007', 'The Sorting Hat', 'rr-009'),
('ch-009-008', 'The Potions Master', 'rr-009'),
('ch-009-009', 'The Midnight Duel', 'rr-009'),
('ch-009-010', 'Halloween', 'rr-009'),
('ch-009-011', 'Quidditch', 'rr-009'),
('ch-009-012', 'The Mirror of Erised', 'rr-009'),
('ch-009-013', 'Nicolas Flamel', 'rr-009'),
('ch-009-014', 'Norbert the Norwegian Ridgeback', 'rr-009'),
('ch-009-015', 'The Forbidden Forest', 'rr-009'),
('ch-009-016', 'Through the Trapdoor', 'rr-009'),
('ch-009-017', 'The Man with Two Faces', 'rr-009');


-- ===========================================
-- Harry Potter and the Chamber of Secrets (work-010, rr-010) — 18 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-010-001', 'The Worst Birthday', 'rr-010'),
('ch-010-002', 'Dobby''s Warning', 'rr-010'),
('ch-010-003', 'The Burrow', 'rr-010'),
('ch-010-004', 'At Flourish and Blotts', 'rr-010'),
('ch-010-005', 'The Whomping Willow', 'rr-010'),
('ch-010-006', 'Gilderoy Lockhart', 'rr-010'),
('ch-010-007', 'Mudbloods and Murmurs', 'rr-010'),
('ch-010-008', 'The Deathday Party', 'rr-010'),
('ch-010-009', 'The Writing on the Wall', 'rr-010'),
('ch-010-010', 'The Rogue Bludger', 'rr-010'),
('ch-010-011', 'The Dueling Club', 'rr-010'),
('ch-010-012', 'The Polyjuice Potion', 'rr-010'),
('ch-010-013', 'The Very Secret Diary', 'rr-010'),
('ch-010-014', 'Cornelius Fudge', 'rr-010'),
('ch-010-015', 'Aragog', 'rr-010'),
('ch-010-016', 'The Chamber of Secrets', 'rr-010'),
('ch-010-017', 'The Heir of Slytherin', 'rr-010'),
('ch-010-018', 'Dobby''s Reward', 'rr-010');


-- ===========================================
-- Harry Potter and the Prisoner of Azkaban (work-011, rr-011) — 22 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-011-001', 'Owl Post', 'rr-011'),
('ch-011-002', 'Aunt Marge''s Big Mistake', 'rr-011'),
('ch-011-003', 'The Knight Bus', 'rr-011'),
('ch-011-004', 'The Leaky Cauldron', 'rr-011'),
('ch-011-005', 'The Dementor', 'rr-011'),
('ch-011-006', 'Talons and Tea Leaves', 'rr-011'),
('ch-011-007', 'The Boggart in the Wardrobe', 'rr-011'),
('ch-011-008', 'Flight of the Fat Lady', 'rr-011'),
('ch-011-009', 'Grim Defeat', 'rr-011'),
('ch-011-010', 'The Marauder''s Map', 'rr-011'),
('ch-011-011', 'The Firebolt', 'rr-011'),
('ch-011-012', 'The Patronus', 'rr-011'),
('ch-011-013', 'Gryffindor versus Ravenclaw', 'rr-011'),
('ch-011-014', 'Snape''s Grudge', 'rr-011'),
('ch-011-015', 'The Quidditch Final', 'rr-011'),
('ch-011-016', 'Professor Trelawney''s Prediction', 'rr-011'),
('ch-011-017', 'Cat, Rat, and Dog', 'rr-011'),
('ch-011-018', 'Moony, Wormtail, Padfoot, and Prongs', 'rr-011'),
('ch-011-019', 'The Servant of Lord Voldemort', 'rr-011'),
('ch-011-020', 'The Dementor''s Kiss', 'rr-011'),
('ch-011-021', 'Hermione''s Secret', 'rr-011'),
('ch-011-022', 'Owl Post Again', 'rr-011');


-- ===========================================
-- Harry Potter and the Goblet of Fire (work-012, rr-012) — 37 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-012-001', 'The Riddle House', 'rr-012'),
('ch-012-002', 'The Scar', 'rr-012'),
('ch-012-003', 'The Invitation', 'rr-012'),
('ch-012-004', 'Back to The Burrow', 'rr-012'),
('ch-012-005', 'Weasleys'' Wizard Wheezes', 'rr-012'),
('ch-012-006', 'The Portkey', 'rr-012'),
('ch-012-007', 'Bagman and Crouch', 'rr-012'),
('ch-012-008', 'The Quidditch World Cup', 'rr-012'),
('ch-012-009', 'The Dark Mark', 'rr-012'),
('ch-012-010', 'Mayhem at the Ministry', 'rr-012'),
('ch-012-011', 'Aboard the Hogwarts Express', 'rr-012'),
('ch-012-012', 'The Triwizard Tournament', 'rr-012'),
('ch-012-013', 'Mad-Eye Moody', 'rr-012'),
('ch-012-014', 'The Unforgivable Curses', 'rr-012'),
('ch-012-015', 'Beauxbatons and Durmstrang', 'rr-012'),
('ch-012-016', 'The Goblet of Fire', 'rr-012'),
('ch-012-017', 'The Four Champions', 'rr-012'),
('ch-012-018', 'The Weighing of the Wands', 'rr-012'),
('ch-012-019', 'The Hungarian Horntail', 'rr-012'),
('ch-012-020', 'The First Task', 'rr-012'),
('ch-012-021', 'The House-Elf Liberation Front', 'rr-012'),
('ch-012-022', 'The Unexpected Task', 'rr-012'),
('ch-012-023', 'The Yule Ball', 'rr-012'),
('ch-012-024', 'Rita Skeeter''s Scoop', 'rr-012'),
('ch-012-025', 'The Egg and the Eye', 'rr-012'),
('ch-012-026', 'The Second Task', 'rr-012'),
('ch-012-027', 'Padfoot Returns', 'rr-012'),
('ch-012-028', 'The Madness of Mr. Crouch', 'rr-012'),
('ch-012-029', 'The Dream', 'rr-012'),
('ch-012-030', 'The Pensieve', 'rr-012'),
('ch-012-031', 'The Third Task', 'rr-012'),
('ch-012-032', 'Flesh, Blood, and Bone', 'rr-012'),
('ch-012-033', 'The Death Eaters', 'rr-012'),
('ch-012-034', 'Priori Incantatem', 'rr-012'),
('ch-012-035', 'Veritaserum', 'rr-012'),
('ch-012-036', 'The Parting of the Ways', 'rr-012'),
('ch-012-037', 'The Beginning', 'rr-012');


-- ===========================================
-- Harry Potter and the Order of the Phoenix (work-013, rr-013) — 38 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-013-001', 'Dudley Demented', 'rr-013'),
('ch-013-002', 'A Peck of Owls', 'rr-013'),
('ch-013-003', 'The Advance Guard', 'rr-013'),
('ch-013-004', 'Number Twelve, Grimmauld Place', 'rr-013'),
('ch-013-005', 'The Order of the Phoenix', 'rr-013'),
('ch-013-006', 'The Noble and Most Ancient House of Black', 'rr-013'),
('ch-013-007', 'The Ministry of Magic', 'rr-013'),
('ch-013-008', 'The Hearing', 'rr-013'),
('ch-013-009', 'The Woes of Mrs. Weasley', 'rr-013'),
('ch-013-010', 'Luna Lovegood', 'rr-013'),
('ch-013-011', 'The Sorting Hat''s New Song', 'rr-013'),
('ch-013-012', 'Professor Umbridge', 'rr-013'),
('ch-013-013', 'Detention with Dolores', 'rr-013'),
('ch-013-014', 'Percy and Padfoot', 'rr-013'),
('ch-013-015', 'The Hogwarts High Inquisitor', 'rr-013'),
('ch-013-016', 'In the Hog''s Head', 'rr-013'),
('ch-013-017', 'Educational Decree Number Twenty-Four', 'rr-013'),
('ch-013-018', 'Dumbledore''s Army', 'rr-013'),
('ch-013-019', 'The Lion and the Serpent', 'rr-013'),
('ch-013-020', 'Hagrid''s Tale', 'rr-013'),
('ch-013-021', 'The Eye of the Snake', 'rr-013'),
('ch-013-022', 'St. Mungo''s Hospital for Magical Maladies and Injuries', 'rr-013'),
('ch-013-023', 'Christmas on the Closed Ward', 'rr-013'),
('ch-013-024', 'Occlumency', 'rr-013'),
('ch-013-025', 'The Beetle at Bay', 'rr-013'),
('ch-013-026', 'Seen and Unforeseen', 'rr-013'),
('ch-013-027', 'The Centaur and the Sneak', 'rr-013'),
('ch-013-028', 'Snape''s Worst Memory', 'rr-013'),
('ch-013-029', 'Career Advice', 'rr-013'),
('ch-013-030', 'Grawp', 'rr-013'),
('ch-013-031', 'O.W.L.s', 'rr-013'),
('ch-013-032', 'Out of the Fire', 'rr-013'),
('ch-013-033', 'Fight and Flight', 'rr-013'),
('ch-013-034', 'The Department of Mysteries', 'rr-013'),
('ch-013-035', 'Beyond the Veil', 'rr-013'),
('ch-013-036', 'The Only One He Ever Feared', 'rr-013'),
('ch-013-037', 'The Lost Prophecy', 'rr-013'),
('ch-013-038', 'The Second War Begins', 'rr-013');


-- ===========================================
-- Harry Potter and the Half-Blood Prince (work-014, rr-014) — 30 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-014-001', 'The Other Minister', 'rr-014'),
('ch-014-002', 'Spinner''s End', 'rr-014'),
('ch-014-003', 'Will and Won''t', 'rr-014'),
('ch-014-004', 'Horace Slughorn', 'rr-014'),
('ch-014-005', 'An Excess of Phlegm', 'rr-014'),
('ch-014-006', 'Draco''s Detour', 'rr-014'),
('ch-014-007', 'The Slug Club', 'rr-014'),
('ch-014-008', 'Snape Victorious', 'rr-014'),
('ch-014-009', 'The Half-Blood Prince', 'rr-014'),
('ch-014-010', 'The House of Gaunt', 'rr-014'),
('ch-014-011', 'Hermione''s Helping Hand', 'rr-014'),
('ch-014-012', 'Silver and Opals', 'rr-014'),
('ch-014-013', 'The Secret Riddle', 'rr-014'),
('ch-014-014', 'Felix Felicis', 'rr-014'),
('ch-014-015', 'The Unbreakable Vow', 'rr-014'),
('ch-014-016', 'A Very Frosty Christmas', 'rr-014'),
('ch-014-017', 'A Sluggish Memory', 'rr-014'),
('ch-014-018', 'Birthday Surprises', 'rr-014'),
('ch-014-019', 'Elf Tails', 'rr-014'),
('ch-014-020', 'Lord Voldemort''s Request', 'rr-014'),
('ch-014-021', 'The Unknowable Room', 'rr-014'),
('ch-014-022', 'After the Burial', 'rr-014'),
('ch-014-023', 'Horcruxes', 'rr-014'),
('ch-014-024', 'Sectumsempra', 'rr-014'),
('ch-014-025', 'The Seer Overheard', 'rr-014'),
('ch-014-026', 'The Cave', 'rr-014'),
('ch-014-027', 'The Lightning-Struck Tower', 'rr-014'),
('ch-014-028', 'Flight of the Prince', 'rr-014'),
('ch-014-029', 'The Phoenix Lament', 'rr-014'),
('ch-014-030', 'The White Tomb', 'rr-014');


-- ===========================================
-- Harry Potter and the Deathly Hallows (work-015, rr-015) — 37 chapters
-- ===========================================
INSERT INTO chapters (id, title, resource_id) VALUES
('ch-015-001', 'The Dark Lord Ascending', 'rr-015'),
('ch-015-002', 'In Memoriam', 'rr-015'),
('ch-015-003', 'The Dursleys Departing', 'rr-015'),
('ch-015-004', 'The Seven Potters', 'rr-015'),
('ch-015-005', 'Fallen Warrior', 'rr-015'),
('ch-015-006', 'The Ghoul in Pajamas', 'rr-015'),
('ch-015-007', 'The Will of Albus Dumbledore', 'rr-015'),
('ch-015-008', 'The Wedding', 'rr-015'),
('ch-015-009', 'A Place to Hide', 'rr-015'),
('ch-015-010', 'Kreacher''s Tale', 'rr-015'),
('ch-015-011', 'The Bribe', 'rr-015'),
('ch-015-012', 'Magic is Might', 'rr-015'),
('ch-015-013', 'The Muggle-Born Registration Commission', 'rr-015'),
('ch-015-014', 'The Thief', 'rr-015'),
('ch-015-015', 'The Goblin''s Revenge', 'rr-015'),
('ch-015-016', 'Godric''s Hollow', 'rr-015'),
('ch-015-017', 'Bathilda''s Secret', 'rr-015'),
('ch-015-018', 'The Life and Lies of Albus Dumbledore', 'rr-015'),
('ch-015-019', 'The Silver Doe', 'rr-015'),
('ch-015-020', 'Xenophilius Lovegood', 'rr-015'),
('ch-015-021', 'The Tale of the Three Brothers', 'rr-015'),
('ch-015-022', 'The Deathly Hallows', 'rr-015'),
('ch-015-023', 'Malfoy Manor', 'rr-015'),
('ch-015-024', 'The Wandmaker', 'rr-015'),
('ch-015-025', 'Shell Cottage', 'rr-015'),
('ch-015-026', 'Gringotts', 'rr-015'),
('ch-015-027', 'The Final Hiding Place', 'rr-015'),
('ch-015-028', 'The Missing Mirror', 'rr-015'),
('ch-015-029', 'The Lost Diadem', 'rr-015'),
('ch-015-030', 'The Sacking of Severus Snape', 'rr-015'),
('ch-015-031', 'The Battle of Hogwarts', 'rr-015'),
('ch-015-032', 'The Elder Wand', 'rr-015'),
('ch-015-033', 'The Prince''s Tale', 'rr-015'),
('ch-015-034', 'The Forest Again', 'rr-015'),
('ch-015-035', 'King''s Cross', 'rr-015'),
('ch-015-036', 'The Flaw in the Plan', 'rr-015'),
('ch-015-037', 'Epilogue: Nineteen Years Later', 'rr-015');
