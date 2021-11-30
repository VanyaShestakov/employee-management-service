package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.ProjectDTO;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectDTO getById(UUID id);

    ProjectDTO getByName(String name);

    List<ProjectDTO> getAll();

    ProjectDTO save(ProjectDTO projectDTO);

    ProjectDTO update(ProjectDTO projectDTO);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    boolean existsByName(String name);
}
