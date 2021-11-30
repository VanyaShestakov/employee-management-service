package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.dto.converter.ProjectConverter;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.exception.NotValidUUIDException;
import com.leverx.employeestat.rest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.leverx.employeestat.rest.controller.tool.UUIDUtils.getUUIDFromString;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final BindingResultParser bindingResultParser;

    @Autowired
    public ProjectController(ProjectService projectService, BindingResultParser bindingResultParser) {
        this.projectService = projectService;
        this.bindingResultParser = bindingResultParser;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO getProject(@PathVariable String id) {
        return projectService.getById(getUUIDFromString(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO postProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException
                    (String.format("Fields of Project have errors: %s", bindingResultParser.getFieldErrMismatches(result)));
        }
        return projectService.save(projectDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProjectDTO putProject(@RequestBody @Valid ProjectDTO projectDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException
                    (String.format("Fields of Project have errors: %s", bindingResultParser.getFieldErrMismatches(result)));
        }
        return projectService.update(projectDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@PathVariable String id) {
        projectService.deleteById(getUUIDFromString(id));
    }

}
