insert into users (id, email, first_name, last_name)
values (1,'YusukeUrameshi@example.com', 'Yusuke', 'Urameshi');

insert into users (id, email, first_name, last_name)
values (2,'KuwabaraKazuma@example.com', 'Kuwabara', 'Kazuma');

insert into profile (id, description, name) values (1,'Manages everything', 'Admin');

insert into user_profile (id, profile_id, user_id) values (1, 1, 1);
insert into user_profile (id, profile_id, user_id) values (2, 1, 2);
