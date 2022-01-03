package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.model.RegistrationRequest;
import com.leverx.employeestat.rest.model.ResetPasswordRequest;
import com.leverx.employeestat.rest.service.AuthorizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Api(tags = {"Authorization"})
public class AuthorizationController {

    private final Logger log = LogManager.getLogger(AuthorizationController.class);

    private final AuthorizationService authorizationService;
    private final BindingResultParser bindingResultParser;

    @Autowired
    public AuthorizationController(AuthorizationService authorizationService, BindingResultParser bindingResultParser) {
        this.authorizationService = authorizationService;
        this.bindingResultParser = bindingResultParser;
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Register employee", response = EmployeeDTO.class)
    public EmployeeDTO registerEmployee(@ApiParam(name = "Registration request", value = "Contains mandatory fields for registration of employee")
                                        @RequestBody
                                        @Valid RegistrationRequest request, BindingResult result) {
        log.info("executing registerEmployee() method");
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields of Employee have errors: " + bindingResultParser.getFieldErrMismatches(result));
        }
        return authorizationService.registerEmployee(request.toDTO());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Reset password of employee")
    public void resetPassword(@ApiParam(name = "Reset password request", value = "Contains username, old and new password of employee")
                              @RequestBody
                              @Valid ResetPasswordRequest request, BindingResult result) {
        log.info("executing resetPassword() method");
        if (result.hasErrors()) {
            throw new NotValidRecordException("Fields have errors: " + bindingResultParser.getFieldErrMismatches(result));
        }
        authorizationService.resetPassword(request);
    }
}
