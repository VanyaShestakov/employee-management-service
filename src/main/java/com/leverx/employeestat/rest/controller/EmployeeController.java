package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.leverx.employeestat.rest.controller.tool.UUIDUtils.getUUIDFromString;

@RestController
@RequestMapping("/api/employees")
@Api(tags = {"Employee CRUD operations"})
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BindingResultParser bindingResultParser;

    @Autowired
    public EmployeeController(EmployeeService employeeService, BindingResultParser bindingResultParser) {
        this.employeeService = employeeService;
        this.bindingResultParser = bindingResultParser;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get list of all employees")
    public List<EmployeeDTO> getAllEmployees() {
        log.info("executing getAllEmployees() method");
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get employee by id")
    public EmployeeDTO getEmployee(@ApiParam(value = "Id of received employee")
                                   @PathVariable String id) {
        log.info("executing getEmployee() method");
        return employeeService.getById(getUUIDFromString(id));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update employee")
    public EmployeeDTO putEmployee(@ApiParam(value = "Contains all information of employee with changed fields", name = "Employee")
                                   @RequestBody
                                   @Valid EmployeeDTO employeeDTO, BindingResult result) {
        log.info("executing putEmployee() method");
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields of Employee have errors: " + bindingResultParser.getFieldErrMismatches(result));
        }
        return employeeService.update(employeeDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete employee")
    public void deleteEmployee(@ApiParam(value = "Id of deleting employee")
                               @PathVariable String id) {
        log.info("executing deleteEmployee() method");
        employeeService.deleteById(getUUIDFromString(id));
    }
}
