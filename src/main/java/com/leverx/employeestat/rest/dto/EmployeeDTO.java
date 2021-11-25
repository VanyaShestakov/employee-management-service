package com.leverx.employeestat.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String position;
    private String role;
    private UUID departmentId;
    private List<UUID> projectIds;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public List<UUID> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<UUID> projectIds) {
        this.projectIds = projectIds;
    }

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
}
