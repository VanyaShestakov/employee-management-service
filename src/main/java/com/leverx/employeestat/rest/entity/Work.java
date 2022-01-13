package com.leverx.employeestat.rest.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "project_employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Work {

    @EmbeddedId
    private WorkId id;

    @Column(name = "position_start_date")
    private LocalDate positionStartDate;

    @Column(name = "position_end_date")
    private LocalDate positionEndDate;

    @Column(name = "working_hours")
    private Integer workingHours;

}
