package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.EntityConversionException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DepartmentConverter {

    private final EmployeeService employeeService;

    @Autowired
    public DepartmentConverter(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public Department toEntity(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setId(departmentDTO.getId());
        if (departmentDTO.getEmployeeIds() != null) {
            for (UUID id : departmentDTO.getEmployeeIds()) {
                try {
                    department.addEmployee(employeeService.getById(id));
                } catch (NoSuchRecordException e) {
                    throw new EntityConversionException(e.getMessage(), e);
                }
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
                try {
                    departmentDTO.addEmployeeId(employee.getId());
                } catch (NoSuchRecordException e) {
                    throw new EntityConversionException(e.getMessage(), e);
                }
            }
        }
        return departmentDTO;
    }
}
