package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    public List<Employee> getAll();

    public Employee getById(UUID id);

    public Employee getByUsername(String username);

    public Employee save(Employee employee);

    public Employee update(Employee employee);

    public void deleteById(UUID id);

    public boolean existsById(UUID id);
}
