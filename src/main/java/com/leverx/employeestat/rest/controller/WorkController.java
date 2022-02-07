package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.WorkDTO;
import com.leverx.employeestat.rest.service.WorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.leverx.employeestat.rest.controller.tool.UUIDUtils.getUUIDFromString;

@RestController
@RequestMapping("/api/works")
@Api(tags = "Work CRUD operations")
@Slf4j
public class WorkController {

    private final WorkService workService;

    @Autowired
    public WorkController(WorkService workService) {
        this.workService = workService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get list of all works")
    public List<WorkDTO> getAllWorks() {
        log.info("executing getAllWorks() method");
        return workService.getAll();
    }

    @GetMapping("/{empId}/{projId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get work by employee id and project id")
    public WorkDTO getWork(@ApiParam(value = "Employee id (UUID)")
                           @PathVariable("empId") String employeeId,
                           @ApiParam(value = "Project id (UUID)")
                           @PathVariable("projId") String projectId) {
        log.info("executing getWork() method");
        return workService.getByIds(getUUIDFromString(employeeId), getUUIDFromString(projectId));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update work")
    public WorkDTO putWork(@ApiParam(value = "Contains all information of employee work with changed fields", name = "Work")
                           @RequestBody WorkDTO workDTO) {
        log.info("executing putWork() method");
        return workService.update(workDTO);
    }

    @DeleteMapping("/{empId}/{projId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Get work by employee id and project id")
    public void deleteWork(@ApiParam(value = "Employee id (UUID)")
                           @PathVariable("empId") String employeeId,
                           @ApiParam(value = "Project id (UUID)")
                           @PathVariable("projId") String projectId) {
        log.info("executing deleteWork() method");
        workService.deleteByIds(getUUIDFromString(employeeId), getUUIDFromString(projectId));
    }
}
