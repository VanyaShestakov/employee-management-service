package com.leverx.employeestat.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApiModel(value = "Project", description = "Contains all information about project and its employees(ids)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {

    private UUID id;

    @NotBlank
    private String name;

    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate begin;

    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate end;

    private List<UUID> employeeIds;

    public void addEmployeeId(UUID id) {
        if (employeeIds == null) {
            employeeIds = new ArrayList<>();
        }
        employeeIds.add(id);
    }
}
