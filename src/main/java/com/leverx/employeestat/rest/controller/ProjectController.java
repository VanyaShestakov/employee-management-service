package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.dto.converter.ProjectConverter;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO getProject(@PathVariable UUID id) {
        return projectService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO postProject(@RequestBody ProjectDTO projectDTO) {
        return projectService.save(projectDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO putProject(@RequestBody ProjectDTO projectDTO) {
        return projectService.update(projectDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@PathVariable UUID id) {
        projectService.deleteById(id);
    }
}
