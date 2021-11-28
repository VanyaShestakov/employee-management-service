package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.WorkDTO;
import com.leverx.employeestat.rest.entity.Work;
import com.leverx.employeestat.rest.entity.WorkId;
import com.leverx.employeestat.rest.exception.EntityConversionException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.util.UUID;

@Component
public class WorkConverter {

    private final EmployeeRepository employeeRepository;

    private final ProjectRepository projectRepository;

    @Autowired
    public WorkConverter(EmployeeRepository employeeRepository, ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    public Work toEntity(WorkDTO workDTO) {
        Work work = new Work();
        UUID employeeId = workDTO.getEmployeeId();
        UUID projectId = workDTO.getProjectId();
        WorkId workId = new WorkId();
        workId.setEmployee(employeeRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> {
                    throw new EntityConversionException("Employee with id=" + employeeId + " not found");
                }));
        workId.setProject(projectRepository.findProjectById(projectId)
                .orElseThrow(() -> {
                    throw new EntityConversionException("Project with id=" + employeeId + " not found");
                }));
        work.setId(workId);
        work.setPositionStartDate(workDTO.getPositionStartDate());
        work.setPositionEndDate(workDTO.getPositionEndDate());
        work.setWorkingHours(workDTO.getWorkingHours());
        return work;
    }

    public WorkDTO toDTO(Work work) {
        WorkDTO workDTO = new WorkDTO();
        workDTO.setEmployeeId(work.getId().getEmployee().getId());
        workDTO.setProjectId(work.getId().getProject().getId());
        workDTO.setWorkingHours(work.getWorkingHours());
        workDTO.setPositionStartDate(work.getPositionStartDate());
        workDTO.setPositionEndDate(work.getPositionEndDate());
        return workDTO;
    }
}
