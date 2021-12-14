package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.service.AuthorizationService;
import com.leverx.employeestat.rest.service.CSVReaderService;
import com.leverx.employeestat.rest.service.EmployeeService;
import com.leverx.employeestat.rest.service.impl.CSVReaderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/employees/upload")
@Api(tags = {"API for uploading employees from CSV file"})
public class CSVUploadController {

    private final CSVReaderService readerService;
    private final AuthorizationService authorizationService;

    @Autowired
    public CSVUploadController(CSVReaderService readerService, AuthorizationService authorizationService) {
        this.readerService = readerService;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Upload employees")
    public List<EmployeeDTO> uploadEmployees(@ApiParam(value = "File with .csv extension, that contains table with employees")
                                             @RequestParam("file") MultipartFile file) {
        List<EmployeeDTO> employees = readerService.getEmployeesFromFile(file);
        return authorizationService.registerAll(employees);
    }
}
