package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.WorkDTO;

import java.util.List;
import java.util.UUID;

public interface WorkService {

    List<WorkDTO> getAll();

    WorkDTO getByIds(UUID employeeId, UUID projectId);

    //WorkDTO save(WorkDTO workDTO);

    WorkDTO update(WorkDTO workDTO);

    void deleteByIds(UUID employeeId, UUID projectId);
}
