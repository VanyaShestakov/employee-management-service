package com.leverx.employeestat.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApiModel(value = "Employee", description = "Contains all information about employee and its projects(ids) and department(id)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private UUID id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @JsonIgnore
    private String password;

    @NotBlank
    private String position;

    @NotBlank
    private String role;

    private UUID departmentId;

    private List<UUID> projectIds;

    public void addProjectId(UUID id) {
        if (projectIds == null) {
            projectIds = new ArrayList<>();
        }
        projectIds.add(id);
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", position='" + position + '\'' +
                ", role='" + role + '\'' +
                ", departmentId=" + departmentId +
                ", projectsIds=" + projectIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDTO that = (EmployeeDTO) o;
        return Objects.equals(id, that.id) && firstName.equals(that.firstName) && lastName.equals(that.lastName) && username.equals(that.username) && password.equals(that.password) && position.equals(that.position) && role.equals(that.role) && Objects.equals(departmentId, that.departmentId) && Objects.equals(projectIds, that.projectIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, username, password, position, role, departmentId, projectIds);
    }
}
