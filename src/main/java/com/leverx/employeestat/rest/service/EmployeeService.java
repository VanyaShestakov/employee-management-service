package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.EmployeeDTO;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    public List<EmployeeDTO> getAll();

    public EmployeeDTO getById(UUID id);

    public EmployeeDTO getByUsername(String username);

    public EmployeeDTO save(EmployeeDTO employeeDTO);

    public List<EmployeeDTO> saveAll(List<EmployeeDTO> employeeDTOs);

    public EmployeeDTO update(EmployeeDTO employeeDTO);

    public void deleteById(UUID id);

    public boolean existsById(UUID id);
}
