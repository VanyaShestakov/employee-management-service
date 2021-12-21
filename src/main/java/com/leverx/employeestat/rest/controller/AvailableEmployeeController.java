package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.service.AvailableEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees/available")
@Api(tags = {"API for retrieving available employees"})
public class AvailableEmployeeController {

    private final Logger log = LogManager.getLogger(AvailableEmployeeController.class);

    private final AvailableEmployeeService availableEmployeeService;

    @Autowired
    public AvailableEmployeeController(AvailableEmployeeService availableEmployeeService) {
        this.availableEmployeeService = availableEmployeeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get available employees at the current moment")
    public List<EmployeeDTO> getAvailableEmployeesNow() {
        log.info("executing getAvailableEmployeesNow() method");
        return availableEmployeeService.getAvailableEmployeesNow();
    }

    @GetMapping("/{days}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get available employees for the next amount of days")
    public List<EmployeeDTO> getAvailableEmployeesNext(@ApiParam(value = "Amount of the next days starting today")
                                                       @PathVariable("days") int days) {
        log.info("executing getAvailableEmployeesNext() method");
        return availableEmployeeService.getAvailableEmployeesNext(days);
    }
}
