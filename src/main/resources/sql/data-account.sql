-- noinspection SqlResolveForFile
-- noinspection SqlResolveForFile @ column/"FIRST_NAME"
-- noinspection SqlResolveForFile @ column/"LAST_NAME"


insert into ACCOUNT(ID, USER, NAME, AMOUNT) values (1, 1, 'Main account', 150);
insert into ACCOUNT(ID, USER, NAME, AMOUNT) values (2, 1, 'For mobile', 1000);

insert into ACCOUNT(ID, USER, NAME, AMOUNT) values (10, 2, 'Abc', 650);

commit;
