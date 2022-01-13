package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DepartmentConverter {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentConverter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Department toEntity(DepartmentDTO departmentDTO) {
        Department department = Department.builder()
                .name(departmentDTO.getName())
                .id(departmentDTO.getId())
                .build();

        Optional.ofNullable(departmentDTO.getEmployeeIds())
                .ifPresent(employeeIds -> employeeIds.forEach(id -> {
                    department.addEmployee(employeeRepository.findEmployeeById(id)
                            .orElseThrow(() -> new NoSuchRecordException
                                    (String.format("Employee with id=%s not found", id)))
                    );
                }));

        return department;
    }

    public DepartmentDTO toDTO(Department department) {
        DepartmentDTO departmentDTO = DepartmentDTO.builder()
                .name(department.getName())
                .id(department.getId())
                .build();

        Optional.ofNullable(department.getEmployees())
                .ifPresent(employees -> employees.forEach(employee -> departmentDTO.addEmployeeId(employee.getId())));

        return departmentDTO;
    }
}
