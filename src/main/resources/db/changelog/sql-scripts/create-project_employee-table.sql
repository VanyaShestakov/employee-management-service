create table project_employee (
    employee_id uuid not null,
    project_id uuid not null,
    position_start_date date,
    position_end_date date,
    working_hours int,
    primary key (employee_id, project_id),
    foreign key (employee_id) references employee (id),
    foreign key (project_id) references project (id)
)