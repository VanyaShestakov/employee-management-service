package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.DuplicateProjectException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import com.leverx.employeestat.rest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Project getById(UUID id) {
        return projectRepository.findProjectById(id)
                .orElseThrow(() -> {
                    throw new NoSuchRecordException("Project with id=" + id + " not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Project getByName(String name) {
        return projectRepository.findProjectByName(name).
                orElseThrow(() -> {
                    throw new NoSuchRecordException("Project with name=" + name + " not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project save(Project project) {
        if (projectRepository.existsByName(project.getName())) {
            throw new DuplicateProjectException("Project with name=" + project.getName() + " already exists");
        }
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public Project update(Project project) {
        if (projectRepository.existsById(project.getId())) {
            return projectRepository.save(project);
        } else if (!projectRepository.existsByName(project.getName())) {
            return projectRepository.save(project);
        } else {
            throw new DuplicateProjectException("Project with name=" + project.getName() + " already exists");
        }
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new NoSuchRecordException("Project with id=" + id + " not found for deleting");
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
