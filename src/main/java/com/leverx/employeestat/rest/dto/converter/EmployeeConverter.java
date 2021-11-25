package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.EntityConversionException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.service.DepartmentService;
import com.leverx.employeestat.rest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class EmployeeConverter {

    private final DepartmentService departmentService;
    private final ProjectService projectService;

    @Autowired
    public EmployeeConverter(DepartmentService departmentService, ProjectService projectService) {
        this.departmentService = departmentService;
        this.projectService = projectService;
    }

    public Employee toEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setUsername(employeeDTO.getUsername());
        employee.setId(employeeDTO.getId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setPassword(employeeDTO.getPassword());
        employee.setPosition(employeeDTO.getPosition());
        if (employeeDTO.getDepartmentId() != null) {
            try {
                Department department = departmentService.getById(employeeDTO.getDepartmentId());
                employee.setDepartment(department);
            } catch (NoSuchRecordException e) {
                throw new EntityConversionException(e.getMessage(), e);
            }
        }
        if (employeeDTO.getProjectIds() != null) {
            for (UUID id : employeeDTO.getProjectIds()) {
                try {
                    employee.addProject(projectService.getById(id));
                } catch (NoSuchRecordException e) {
                    throw new EntityConversionException(e.getMessage(), e);
                }
            }
        }
        return employee;
    }

    public EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername(employee.getUsername());
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setPassword(employee.getPassword());
        employeeDTO.setPosition(employee.getPosition());
        Department department = employee.getDepartment();
        if (department != null) {
            employeeDTO.setDepartmentId(department.getId());
        }
        List<Project> projects = employee.getProjects();
        if (projects != null) {
            for (Project project : projects) {
                employeeDTO.addProjectId(project.getId());
            }
        }
        return employeeDTO;
    }
}
