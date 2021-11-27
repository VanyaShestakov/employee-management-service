package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.entity.Department;
import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    DepartmentDTO save(DepartmentDTO departmentDTO);

    void deleteById(UUID id);

    DepartmentDTO update(DepartmentDTO departmentDTO);

    List<DepartmentDTO> getAll();

    DepartmentDTO getById(UUID id);

    boolean existsById(UUID id);

    boolean existsByName(String name);
}
