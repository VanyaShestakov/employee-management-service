package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.service.EmployeeService;
import com.leverx.employeestat.rest.service.impl.CSVReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/employees/upload")
public class CSVUploadController {

    private final CSVReaderService readerService;
    private final EmployeeService employeeService;
    private final PasswordEncoder encoder;

    @Autowired
    public CSVUploadController(CSVReaderService readerService,
                               EmployeeService employeeService,
                               PasswordEncoder encoder) {
        this.readerService = readerService;
        this.employeeService = employeeService;
        this.encoder = encoder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<EmployeeDTO> uploadEmployees(@RequestParam("file") MultipartFile file) {
        List<EmployeeDTO> employees = readerService.getEmployeesFromFile(file);
        employees.stream().forEach(e -> e.setPassword(encoder.encode(e.getPassword())));
        return employeeService.saveAll(employees);
    }
}
