begin;

delete from chapters
where id like 'ch-0__-0__';

delete from reading_resource
where id like 'rr-0__';

commit;

