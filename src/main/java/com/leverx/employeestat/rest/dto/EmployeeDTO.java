package com.leverx.employeestat.rest.dto;

import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.entity.Role;

import javax.persistence.*;
import java.util.List;

public class EmployeeDTO {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String position;
    private String role;
    private String departmentId;
    private List<String> projectsIds;

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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public List<String> getProjectsIds() {
        return projectsIds;
    }

    public void setProjectsIds(List<String> projectsIds) {
        this.projectsIds = projectsIds;
    }
}
