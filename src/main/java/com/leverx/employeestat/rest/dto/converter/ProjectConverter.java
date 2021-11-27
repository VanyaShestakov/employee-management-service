package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.ProjectDTO;
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
public class ProjectConverter {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProjectConverter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Project toEntity(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        project.setBegin(projectDTO.getBegin());
        project.setEnd(projectDTO.getEnd());
        if (projectDTO.getEmployeeIds() != null) {
            for (UUID id : projectDTO.getEmployeeIds()) {
                project.addEmployee(employeeRepository.findEmployeeById(id)
                        .orElseThrow(() -> {
                            throw new NoSuchRecordException("Employee with id=" + id + " not found");
                        }));
            }
        }
        return project;
    }

    public ProjectDTO toDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(project.getName());
        projectDTO.setBegin(project.getBegin());
        projectDTO.setEnd(project.getEnd());
        projectDTO.setId(project.getId());

        List<Employee> employees = project.getEmployees();
        if (employees != null) {
            for (Employee employee : employees) {
                try {
                    projectDTO.addEmployeeId(employee.getId());
                } catch (NoSuchRecordException e) {
                    throw new EntityConversionException(e.getMessage(), e);
                }
            }
        }
        return projectDTO;
    }
}
