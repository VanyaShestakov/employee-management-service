package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProjectConverter {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProjectConverter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Project toEntity(ProjectDTO projectDTO) {
        Project project = Project.builder()
                .id(projectDTO.getId())
                .name(projectDTO.getName())
                .begin(projectDTO.getBegin())
                .end(projectDTO.getEnd())
                .build();

        Optional.ofNullable(projectDTO.getEmployeeIds())
                .ifPresent(employeeIds -> employeeIds.forEach(id -> {
                    project.addEmployee(employeeRepository.findEmployeeById(id)
                            .orElseThrow(() -> new NoSuchRecordException
                                    (String.format("Employee with id=%s not found", id)))
                    );
                }));

        return project;
    }

    public ProjectDTO toDTO(Project project) {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .name(project.getName())
                .begin(project.getBegin())
                .end(project.getEnd())
                .id(project.getId())
                .build();

        Optional.ofNullable(project.getEmployees())
                .ifPresent(employees -> employees.forEach(employee -> projectDTO.addEmployeeId(employee.getId())));

        return projectDTO;
    }
}
