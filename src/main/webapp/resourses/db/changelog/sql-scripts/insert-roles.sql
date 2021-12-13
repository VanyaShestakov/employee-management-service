CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
insert into role (id, name) values (uuid_generate_v4(), 'ROLE_EMPLOYEE');
insert into role (id, name) values (uuid_generate_v4(), 'ROLE_MANAGER');