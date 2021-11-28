package com.leverx.employeestat.rest.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "project_employee")
public class Work {

    @EmbeddedId
    private WorkId id;

    @Column(name = "position_start_date")
    private Date positionStartDate;

    @Column(name = "position_end_date")
    private Date positionEndDate;

    @Column(name = "working_hours")
    private Integer workingHours;

    public WorkId getId() {
        return id;
    }

    public void setId(WorkId id) {
        this.id = id;
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

    public Integer getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Integer workingHours) {
        this.workingHours = workingHours;
    }
}
