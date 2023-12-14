
-- create schema BANK;

create user BANK_USER password 'bankPassword1';

create role BANK_UPDATE_ROLE;

grant BANK_UPDATE_ROLE to BANK_USER;

-- GRANT SELECT ON TABLE BANK.USER TO BANK_UPDATE_ROLE;
-- GRANT UPDATE ON TABLE BANK.USER TO BANK_UPDATE_ROLE;

-- GRANT SELECT, UPDATE ON TABLE atable TO approle;
-- GRANT USAGE ON SEQUENCE asequence to approle;
-- GRANT EXECUTE ON ROUTINE aroutine TO approle;



--- - $statement separator$ ----
--
-- !! I do not know why... but it should be called as separate jdbc statement !!
--
-- alter user BANK_USER set initial schema BANK;
