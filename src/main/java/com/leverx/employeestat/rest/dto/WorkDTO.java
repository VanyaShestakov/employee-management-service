package com.leverx.employeestat.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@ApiModel(value = "Works", description = "Representation of the workload of employees on the project")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkDTO {

    private UUID employeeId;

    private UUID projectId;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate positionStartDate;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate positionEndDate;

    private Integer workingHours;

}
