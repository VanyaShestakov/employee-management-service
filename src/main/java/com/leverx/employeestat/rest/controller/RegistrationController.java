package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final EmployeeService employeeService;
    private final BindingResultParser bindingResultParser;
    private final PasswordEncoder encoder;

    @Autowired
    public RegistrationController(EmployeeService employeeService,
                                  BindingResultParser bindingResultParser,
                                  PasswordEncoder encoder) {
        this.employeeService = employeeService;
        this.bindingResultParser = bindingResultParser;
        this.encoder = encoder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO postEmployee(@RequestBody @Valid EmployeeDTO employeeDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields of Employee have errors: " +
                    bindingResultParser.getFieldErrMismatches(result));
        }
        employeeDTO.setPassword(encoder.encode(employeeDTO.getPassword()));
        return employeeService.save(employeeDTO);
    }
}
