package com.leverx.employeestat.rest.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Reset password request", description = "Contains username, old and new password of employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

}
