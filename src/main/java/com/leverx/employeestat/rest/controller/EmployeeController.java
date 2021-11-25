package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.dto.converter.EmployeeConverter;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeConverter employeeConverter;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeConverter employeeConverter) {
        this.employeeService = employeeService;
        this.employeeConverter = employeeConverter;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAll()
                .stream()
                .map(employeeConverter::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO getEmployee(@PathVariable UUID id) {
        return employeeConverter.toDTO(employeeService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO postEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeConverter.toEntity(employeeDTO);
        return employeeConverter.toDTO(employeeService.save(employee));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO putEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeConverter.toEntity(employeeDTO);
        return employeeConverter.toDTO(employeeService.update(employee));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteById(id);
    }
}
