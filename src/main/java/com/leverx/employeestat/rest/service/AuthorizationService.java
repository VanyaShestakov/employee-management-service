package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.model.ResetPasswordRequest;

import java.util.List;

public interface AuthorizationService {

    EmployeeDTO registerEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO resetPassword(ResetPasswordRequest request);

    List<EmployeeDTO> registerAll(List<EmployeeDTO> employeeDTOs);
}
