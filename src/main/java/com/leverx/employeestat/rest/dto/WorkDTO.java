package com.leverx.employeestat.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;

import java.time.LocalDate;
import java.util.UUID;

@ApiModel(value = "Works", description = "Representation of the workload of employees on the project")
public class WorkDTO {

    private UUID employeeId;

    private UUID projectId;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate positionStartDate;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate positionEndDate;

    private Integer workingHours;

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

    public LocalDate getPositionStartDate() {
        return positionStartDate;
    }

    public void setPositionStartDate(LocalDate positionStartDate) {
        this.positionStartDate = positionStartDate;
    }

    public LocalDate getPositionEndDate() {
        return positionEndDate;
    }

    public void setPositionEndDate(LocalDate positionEndDate) {
        this.positionEndDate = positionEndDate;
    }

    public Integer getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Integer workingHours) {
        this.workingHours = workingHours;
    }
}
