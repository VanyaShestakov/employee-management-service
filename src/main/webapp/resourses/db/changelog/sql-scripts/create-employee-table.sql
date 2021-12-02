create table employee (
    id uuid primary key unique not null,
    first_name varchar(32) not null,
    last_name varchar(32) not null,
    username varchar(32) not null,
    password varchar(256) not null,
    position varchar(32) not null,
    role_id uuid not null,
    department_id uuid,
    foreign key (role_id) references "role" (id),
    foreign key (department_id) references department (id)
)