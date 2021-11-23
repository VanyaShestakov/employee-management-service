package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.entity.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    Department save(Department department);

    void deleteById(UUID id);

    Department update(Department department);

    List<Department> getAll();

    Department getById(UUID id);

    boolean existsById(UUID id);

    boolean existsByName(String name);
}
