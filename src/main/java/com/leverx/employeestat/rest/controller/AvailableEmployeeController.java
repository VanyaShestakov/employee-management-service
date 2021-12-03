package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.service.AvailableEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees/available")
public class AvailableEmployeeController {

    private final AvailableEmployeeService availableEmployeeService;

    @Autowired
    public AvailableEmployeeController(AvailableEmployeeService availableEmployeeService) {
        this.availableEmployeeService = availableEmployeeService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<EmployeeDTO> getAvailableEmployeesNow() {
        return availableEmployeeService.getAvailableEmployeesNow();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{days}")
    public List<EmployeeDTO> getAvailableEmployeesNext(@PathVariable("days") int days) {
        return availableEmployeeService.getAvailableEmployeesNext(days);
    }
}
