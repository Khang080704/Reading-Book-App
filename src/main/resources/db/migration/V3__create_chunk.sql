alter table chapters add column index_order int;
alter table chapters add column content text;

insert into author_details (id, ol_key, birth_day, website, full_name, bio, resource_provider, created_at, last_modify)
values
    ('author-004', 'OL31727A', '1847', 'https://www.bramstocker.com', 'Bram Stoker', 'Bram Stoker was an Irish author, best known for his 1897 Gothic novel Dracula.', 'OPEN_LIBRARY', now(), now());

insert into works (id, work_key, title, description, cover_id, resource_provider)
values
    ('work-016', 'OL85892W', 'Dracula', 'Dracula is an 1897 Gothic horror novel by Irish author Bram Stoker.', 12216503, 'OPEN_LIBRARY');


