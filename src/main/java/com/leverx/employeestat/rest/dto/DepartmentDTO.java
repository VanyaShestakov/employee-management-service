package com.leverx.employeestat.rest.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApiModel(value = "Department", description = "Contains all information about department and its employees(ids)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO {

    private UUID id;

    @NotBlank
    private String name;

    private List<UUID> employeeIds;

    public void addEmployeeId(UUID id) {
        if (employeeIds == null) {
            employeeIds = new ArrayList<>();
        }
        employeeIds.add(id);
    }
}
