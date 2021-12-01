package com.leverx.employeestat.rest.security.request;

import com.leverx.employeestat.rest.dto.EmployeeDTO;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class RegistrationRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String position;

    @NotBlank
    private String role;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public EmployeeDTO toDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(firstName);
        employeeDTO.setLastName(lastName);
        employeeDTO.setPassword(password);
        employeeDTO.setRole(role);
        employeeDTO.setPosition(position);
        employeeDTO.setUsername(username);
        return employeeDTO;
    }
}
