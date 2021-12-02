package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.security.request.RegistrationRequest;
import com.leverx.employeestat.rest.security.request.ResetPasswordRequest;
import com.leverx.employeestat.rest.service.AuthorizationService;
import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthorizationController {

    private final AuthorizationService authorizationService;
    private final BindingResultParser bindingResultParser;

    @Autowired
    public AuthorizationController(AuthorizationService authorizationService, BindingResultParser bindingResultParser) {
        this.authorizationService = authorizationService;
        this.bindingResultParser = bindingResultParser;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO registerEmployee(@RequestBody @Valid RegistrationRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields of Employee have errors: " +
                    bindingResultParser.getFieldErrMismatches(result));
        }
        return authorizationService.registerEmployee(request.toDTO());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@RequestBody @Valid ResetPasswordRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields have errors: " +
                    bindingResultParser.getFieldErrMismatches(result));
        }
        authorizationService.resetPassword(request);
    }
}
