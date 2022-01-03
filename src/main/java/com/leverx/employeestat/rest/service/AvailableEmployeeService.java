package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.EmployeeDTO;

import java.util.List;

public interface AvailableEmployeeService {
    
    List<EmployeeDTO> getAvailableEmployeesNow();

    List<EmployeeDTO> getAvailableEmployeesNext(int days);
}
