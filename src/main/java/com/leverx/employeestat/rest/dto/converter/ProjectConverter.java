package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProjectConverter {

    private final Logger log = LogManager.getLogger(ProjectConverter.class);

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
                            NoSuchRecordException e = new NoSuchRecordException
                                    (String.format("Employee with id=%s not found", id));
                            log.error("Thrown exception", e);
                            throw e;
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
                projectDTO.addEmployeeId(employee.getId());
            }
        }
        return projectDTO;
    }
}
