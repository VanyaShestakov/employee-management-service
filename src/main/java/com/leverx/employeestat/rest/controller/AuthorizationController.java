package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.model.RegistrationRequest;
import com.leverx.employeestat.rest.model.ResetPasswordRequest;
import com.leverx.employeestat.rest.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
//@Tag(name = "contact", description = "the Contact API")
public class AuthorizationController {

    private final AuthorizationService authorizationService;
    private final BindingResultParser bindingResultParser;

    @Autowired
    public AuthorizationController(AuthorizationService authorizationService, BindingResultParser bindingResultParser) {
        this.authorizationService = authorizationService;
        this.bindingResultParser = bindingResultParser;
    }


//    @Operation(summary = "Register a new employee", description = "", tags = { "employee" })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Employee registered",
//            content = @Content(schema = @Schema(implementation = Employee.class))),
//            @ApiResponse(responseCode = "400", description = "Invalid input"),
//            @ApiResponse(responseCode = "409", description = "Employee already exists") })

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
