package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectConverter {

    private final EmployeeService employeeService;

    @Autowired
    public ProjectConverter(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //TODO: toEntity(), toDto()
    public Project toEntity(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setBegin(projectDTO.getBegin());
        project.setEnd(projectDTO.getEnd());
        if (projectDTO.getEmployeeIds() != null) {

        }
        return project;
    }
}
