CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
insert into employee_db_liquibase.public.role (id, name) values (uuid_generate_v4(), 'ROLE_EMPLOYEE');
insert into employee_db_liquibase.public.role (id, name) values (uuid_generate_v4(), 'ROLE_MANAGER');