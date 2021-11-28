package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.dto.converter.EmployeeConverter;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.exception.NotValidUUIDException;
import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
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
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO getEmployee(@PathVariable String id) {
        return employeeService.getById(getUUIDFromString(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO postEmployee(@RequestBody @Valid EmployeeDTO employeeDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields of Employee have errors: " +
                    bindingResultParser.getFieldErrMismatches(result));
        }
        return employeeService.save(employeeDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO putEmployee(@RequestBody @Valid EmployeeDTO employeeDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields of Employee have errors: " +
                    bindingResultParser.getFieldErrMismatches(result));
        }
        return employeeService.update(employeeDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable String id) {
        employeeService.deleteById(getUUIDFromString(id));
    }

    private UUID getUUIDFromString(String id) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new NotValidUUIDException("Value =" + id + " is not UUID", e);
        }
        return uuid;
    }
}
