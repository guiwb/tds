insert into roles(id, name)
values (1, 'ROLE_ADMIN');
insert into roles(id, name)
values (2, 'ROLE_USER');

insert into users(id, name, email, password)
values (7, 'Admin', 'admin@email.com', '$2a$10$GNPFCV7./ZvNwdY8mCBofu7gsBnK1lVUErPLX1qM9W/ljW52sVa6W');
insert into users(id, name, email, password)
values (8, 'User', 'user@email.com', '$2a$10$GNPFCV7./ZvNwdY8mCBofu7gsBnK1lVUErPLX1qM9W/ljW52sVa6W');

insert into user_roles(user_id, role_id)
values (7, 1);
insert into user_roles(user_id, role_id)
values (8, 2);