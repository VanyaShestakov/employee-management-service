package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.dto.WorkDTO;
import com.leverx.employeestat.rest.dto.converter.WorkConverter;
import com.leverx.employeestat.rest.entity.Work;
import com.leverx.employeestat.rest.entity.WorkId;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
import com.leverx.employeestat.rest.exception.EntityConversionException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import com.leverx.employeestat.rest.repository.WorkRepository;
import com.leverx.employeestat.rest.service.WorkService;
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
                .orElseThrow(() -> {
                    throw new NoSuchRecordException("Work with id=" + workId + " not found");
                });
        return converter.toDTO(work);
    }

    @Override
    @Transactional
    public WorkDTO update(WorkDTO workDTO) {
        WorkId workId = createWorkId(workDTO.getEmployeeId(), workDTO.getProjectId());
        Work work = workRepository.findWorkById(workId)
                .orElseThrow(() -> {
                    throw new NoSuchRecordException("Work with id=" + workId + " not found");
                });

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
            throw new NoSuchRecordException("Work with id=" + workId + " not found");
        }
        workRepository.deleteById(createWorkId(employeeId, projectId));
    }

    private WorkId createWorkId(UUID employeeId, UUID projectId) {
        WorkId workId = new WorkId();
        workId.setEmployee(employeeRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> {
                    throw new NoSuchRecordException("Employee with id=" + employeeId + " not found");
                }));
        workId.setProject(projectRepository.findProjectById(projectId)
                .orElseThrow(() -> {
                    throw new NoSuchRecordException("Project with id=" + projectId + " not found");
                }));
        return workId;
    }
}
