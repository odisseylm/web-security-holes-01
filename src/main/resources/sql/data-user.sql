-- noinspection SqlResolveForFile
-- noinspection SqlResolveForFile @ column/"FIRST_NAME"
-- noinspection SqlResolveForFile @ column/"LAST_NAME"



-- predefined users
insert into USER (ID, FIRST_NAME, LAST_NAME) values (1, 'John', 'Smith');
insert into USER (ID, FIRST_NAME, LAST_NAME) values (2, 'Ivan', 'Ivanov');

--
insert into USER (ID, FIRST_NAME, LAST_NAME) values (100, 'Last predefined user', 'Other');

-- example of new generated users
insert into USER (FIRST_NAME, LAST_NAME) values ('other', 'other');

commit;
