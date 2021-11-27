package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    public List<EmployeeDTO> getAll();

    public EmployeeDTO getById(UUID id);

    public EmployeeDTO getByUsername(String username);

    public EmployeeDTO save(EmployeeDTO employeeDTO);

    public EmployeeDTO update(EmployeeDTO employeeDTO);

    public void deleteById(UUID id);

    public boolean existsById(UUID id);
}
