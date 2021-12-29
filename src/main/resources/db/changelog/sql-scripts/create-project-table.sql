create table project (
    id uuid primary key unique not null,
    name varchar(64) not null,
    begin_date date not null,
    end_date date not null
)