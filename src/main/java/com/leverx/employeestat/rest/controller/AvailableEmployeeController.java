package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.service.AvailableEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/available")
public class AvailableEmployeeController {

    private final AvailableEmployeeService availableEmployeeService;

    @Autowired
    public AvailableEmployeeController(AvailableEmployeeService availableEmployeeService) {
        this.availableEmployeeService = availableEmployeeService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/now")
    public List<EmployeeDTO> getAvailableEmployeesNow() {
        return availableEmployeeService.getAvailableEmployeesNow();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/month")
    public List<EmployeeDTO> getAvailableEmployeesWithinMonth() {
        return availableEmployeeService.getAvailableEmployeesWithinMonth();
    }
}
