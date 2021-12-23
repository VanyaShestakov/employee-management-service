package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.dto.WorkDTO;
import com.leverx.employeestat.rest.dto.converter.WorkConverter;
import com.leverx.employeestat.rest.entity.Work;
import com.leverx.employeestat.rest.entity.WorkId;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import com.leverx.employeestat.rest.repository.WorkRepository;
import com.leverx.employeestat.rest.service.WorkService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final WorkConverter converter;

    @Autowired
    public WorkServiceImpl(WorkRepository workRepository,
                           EmployeeRepository employeeRepository,
                           ProjectRepository projectRepository,
                           WorkConverter converter) {
        this.workRepository = workRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.converter = converter;
    }

    @Override
    @Transactional
    public List<WorkDTO> getAll() {
        return workRepository.findAll()
                .stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkDTO getByIds(UUID employeeId, UUID projectId) {
        WorkId workId = createWorkId(employeeId, projectId);
        Work work = workRepository.findWorkById(workId)
                .orElseThrow(() -> new NoSuchRecordException
                        (String.format("Work with id=%s not found", workId))
                );
        return converter.toDTO(work);
    }

    @Override
    @Transactional
    public WorkDTO update(WorkDTO workDTO) {
        WorkId workId = createWorkId(workDTO.getEmployeeId(), workDTO.getProjectId());
        Work work = workRepository.findWorkById(workId)
                .orElseThrow(() -> new NoSuchRecordException
                        (String.format("Work with id=%s not found", workId))
                );

        work.setPositionEndDate(workDTO.getPositionEndDate());
        work.setPositionStartDate(workDTO.getPositionStartDate());
        work.setWorkingHours(workDTO.getWorkingHours());
        return converter.toDTO(workRepository.save(work));
    }

    @Override
    @Transactional
    public void deleteByIds(UUID employeeId, UUID projectId) {
        WorkId workId = createWorkId(employeeId, projectId);
        if (!workRepository.existsById(workId)) {
            throw new NoSuchRecordException
                    (String.format("Work with id=%s not found", workId));
        }
        workRepository.deleteById(createWorkId(employeeId, projectId));
    }

    private WorkId createWorkId(UUID employeeId, UUID projectId) {
        WorkId workId = new WorkId();
        workId.setEmployee(employeeRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new NoSuchRecordException
                        (String.format("Employee with id=%s not found", employeeId)))
        );
        workId.setProject(projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new NoSuchRecordException
                        (String.format("Project with id=%s not found", projectId)))
        );
        return workId;
    }
}
