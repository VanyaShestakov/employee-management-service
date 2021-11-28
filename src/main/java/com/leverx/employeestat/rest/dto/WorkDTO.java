package com.leverx.employeestat.rest.dto;

import java.util.Date;
import java.util.UUID;

public class WorkDTO {

    private UUID employeeId;
    private UUID projectId;
    private Date positionStartDate;
    private Date positionEndDate;
    private int workingHours;

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public Date getPositionStartDate() {
        return positionStartDate;
    }

    public void setPositionStartDate(Date positionStartDate) {
        this.positionStartDate = positionStartDate;
    }

    public Date getPositionEndDate() {
        return positionEndDate;
    }

    public void setPositionEndDate(Date positionEndDate) {
        this.positionEndDate = positionEndDate;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }
}
