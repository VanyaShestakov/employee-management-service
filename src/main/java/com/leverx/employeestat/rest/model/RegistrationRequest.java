package com.leverx.employeestat.rest.model;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Registration request", description = "Contains mandatory fields for registration of employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
