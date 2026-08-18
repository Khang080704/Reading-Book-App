select *
from user_favorite_works u join works w on u.work_id = w.id
join users us on u.user_id = us.id
where us.email = 'test@gmail.com';