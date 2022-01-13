package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.leverx.employeestat.rest.controller.tool.UUIDUtils.getUUIDFromString;

@RestController
@RequestMapping("/api/projects")
@Api(tags = "Project CRUD operations")
@Slf4j
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
    @ApiOperation(value = "Get list of all projects")
    public List<ProjectDTO> getAllProjects() {
        log.info("executing getAllProjects() method");
        return projectService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get project by id")
    public ProjectDTO getProject(@ApiParam(value = "Id of reсeiving project (UUID)")
                                 @PathVariable String id) {
        log.info("executing getProject() method");
        return projectService.getById(getUUIDFromString(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create project")
    public ProjectDTO postProject(@ApiParam(value = "Contains mandatory information of project", name = "Project")
                                  @RequestBody
                                  @Valid ProjectDTO projectDTO, BindingResult result) {
        log.info("executing postProject() method");
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields of Project have errors: " + bindingResultParser.getFieldErrMismatches(result));
        }
        return projectService.save(projectDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update project")
    public ProjectDTO putProject(@ApiParam(value = "Contains all information of project with changed fields", name = "Project")
                                 @RequestBody
                                 @Valid ProjectDTO projectDTO, BindingResult result) {
        log.info("executing putProject() method");
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields of Project have errors: " + bindingResultParser.getFieldErrMismatches(result));
        }
        return projectService.update(projectDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete project")
    public void deleteProject(@ApiParam(value = "Id of deleting project (UUID)")
                              @PathVariable String id) {
        log.info("executing putProject() method");
        projectService.deleteById(getUUIDFromString(id));
    }

}
