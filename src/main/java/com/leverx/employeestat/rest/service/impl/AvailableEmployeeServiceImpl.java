package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.dto.converter.EmployeeConverter;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.repository.WorkRepository;
import com.leverx.employeestat.rest.service.AvailableEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailableEmployeeServiceImpl implements AvailableEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final WorkRepository workRepository;
    private final EmployeeConverter converter;

    @Autowired
    public AvailableEmployeeServiceImpl(EmployeeRepository employeeRepository, WorkRepository workRepository, EmployeeConverter converter) {
        this.employeeRepository = employeeRepository;
        this.workRepository = workRepository;
        this.converter = converter;
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAvailableEmployeesNow() {
        return workRepository.findAllAvailableNow()
                .stream()
                .map(work -> converter.toDTO(work.getId().getEmployee()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EmployeeDTO> getAvailableEmployeesNext(int days) {
        return workRepository.findAllAvailableNext(days)
                .stream()
                .map(work -> converter.toDTO(work.getId().getEmployee()))
                .distinct()
                .collect(Collectors.toList());
    }
}
