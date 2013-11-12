delete from authorities;
delete from users;

insert into users (username, password, enabled) values ('elfleco', 'elfleco', true);
insert into authorities (username, authority) values ('elfleco','ROLE_SUPERVISOR');
insert into authorities (username, authority) values ('elfleco','ROLE_USER');

INSERT INTO users (username, password, enabled) VALUES ('user', 'user', true);
insert into authorities (username, authority) values ('user','ROLE_USER');
