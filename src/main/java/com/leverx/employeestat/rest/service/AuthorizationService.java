package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.model.ResetPasswordRequest;

public interface AuthorizationService {

    EmployeeDTO registerEmployee(EmployeeDTO employeeDTO);

    void resetPassword(ResetPasswordRequest request);
}
