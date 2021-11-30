package com.leverx.employeestat.rest.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "project_employee")
public class Work {

    @EmbeddedId
    private WorkId id;

    @Column(name = "position_start_date")
    private LocalDate positionStartDate;

    @Column(name = "position_end_date")
    private LocalDate positionEndDate;

    @Column(name = "working_hours")
    private Integer workingHours;

    public WorkId getId() {
        return id;
    }

    public void setId(WorkId id) {
        this.id = id;
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
