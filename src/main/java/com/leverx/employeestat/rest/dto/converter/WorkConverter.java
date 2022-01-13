package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.WorkDTO;
import com.leverx.employeestat.rest.entity.Work;
import com.leverx.employeestat.rest.entity.WorkId;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        UUID employeeId = workDTO.getEmployeeId();
        UUID projectId = workDTO.getProjectId();
        WorkId workId = new WorkId();
        workId.setEmployee(employeeRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new NoSuchRecordException
                        (String.format("Employee with id=%s not found", employeeId)))
        );
        workId.setProject(projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new NoSuchRecordException
                        (String.format("Project with id=%s not found", projectId)))
        );

        Work work = Work.builder()
                .id(workId)
                .positionEndDate(workDTO.getPositionEndDate())
                .positionStartDate(workDTO.getPositionStartDate())
                .workingHours(workDTO.getWorkingHours())
                .build();

        return work;
    }

    public WorkDTO toDTO(Work work) {
        return WorkDTO.builder()
                .employeeId(work.getId().getEmployee().getId())
                .projectId(work.getId().getProject().getId())
                .workingHours(work.getWorkingHours())
                .positionEndDate(work.getPositionEndDate())
                .positionStartDate(work.getPositionStartDate())
                .build();
    }
}
