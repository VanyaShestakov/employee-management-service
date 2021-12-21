package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.dto.converter.ProjectConverter;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import com.leverx.employeestat.rest.service.ProjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final Logger log = LogManager.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final ProjectConverter converter;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectConverter converter) {
        this.projectRepository = projectRepository;
        this.converter = converter;
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getById(UUID id) {
        Project project = projectRepository.findProjectById(id)
                .orElseThrow(() -> {
                    NoSuchRecordException e = new NoSuchRecordException
                            (String.format("Project with id=%s not found", id));
                    log.error("Thrown exception", e);
                    throw e;
                });
        return converter.toDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getByName(String name) {
        Project project = projectRepository.findProjectByName(name).
                orElseThrow(() -> {
                    NoSuchRecordException e = new NoSuchRecordException
                            (String.format("Project with name=%s not found", name));
                    log.error("Thrown exception", e);
                    throw e;
                });
        return converter.toDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getAll() {
        return projectRepository.findAll()
                .stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO save(ProjectDTO projectDTO) {
        if (projectRepository.existsByName(projectDTO.getName())) {
            DuplicateRecordException e = new DuplicateRecordException
                    (String.format("Project with name=%s already exists", projectDTO.getName()));
            log.error("Thrown exception", e);
            throw e;
        }
        return converter.toDTO(projectRepository.save(converter.toEntity(projectDTO)));
    }

    @Override
    @Transactional
    public ProjectDTO update(ProjectDTO projectDTO) {
        if (projectDTO.getId() != null && projectRepository.existsById(projectDTO.getId())) {
            return converter.toDTO(projectRepository.save(converter.toEntity(projectDTO)));
        } else if (!projectRepository.existsByName(projectDTO.getName())) {
            return converter.toDTO(projectRepository.save(converter.toEntity(projectDTO)));
        } else {
            DuplicateRecordException e = new DuplicateRecordException
                    (String.format("Project with name=%s already exists", projectDTO.getName()));
            log.error("Thrown exception", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!projectRepository.existsById(id)) {
            NoSuchRecordException e = new NoSuchRecordException
                    (String.format("Department with id=%s not found for deleting", id));
            log.error("Thrown exception", e);
            throw e;
        }
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return projectRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return projectRepository.existsByName(name);
    }
}
