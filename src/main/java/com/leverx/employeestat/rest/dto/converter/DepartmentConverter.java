package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.EntityConversionException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DepartmentConverter {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentConverter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Department toEntity(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setId(departmentDTO.getId());
        if (departmentDTO.getEmployeeIds() != null) {
            for (UUID id : departmentDTO.getEmployeeIds()) {
                department.addEmployee(employeeRepository.findEmployeeById(id)
                        .orElseThrow(() -> {
                            throw new NoSuchRecordException("Employee with id=" + id + " not found");
                        }));
            }
        }
        return department;
    }

    public DepartmentDTO toDTO(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(department.getName());
        departmentDTO.setId(department.getId());
        List<Employee> employees = department.getEmployees();
        if (employees != null) {
            for (Employee employee : employees) {
                departmentDTO.addEmployeeId(employee.getId());
            }
        }
        return departmentDTO;
    }
}
