package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.entity.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    Project getById(UUID id);

    Project getByName(String name);

    List<Project> getAll();

    Project save(Project project);

    Project update(Project project);

    boolean existsById(UUID id);

    boolean existsByName(String name);
}
