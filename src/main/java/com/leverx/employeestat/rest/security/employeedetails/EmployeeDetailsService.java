package com.leverx.employeestat.rest.security.employeedetails;

import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetailsService implements UserDetailsService {

    private final EmployeeDetails employeeDetails;
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeDetailsService(EmployeeDetails employeeDetails, EmployeeService employeeService) {
        this.employeeDetails = employeeDetails;
        this.employeeService = employeeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeDetails.toEmployeeDetails(employeeService.getByUsername(username));
    }
}
