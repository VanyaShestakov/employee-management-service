package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.dto.converter.EmployeeConverter;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter converter;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeConverter converter) {
        this.employeeRepository = employeeRepository;
        this.converter = converter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAll() {
        return employeeRepository.findAll()
                .stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getById(UUID id) {
        Employee employee = employeeRepository.findEmployeeById(id)
                .orElseThrow(() -> {
                    throw new NoSuchRecordException(String.format("Employee with id=%s not found", id));
                });
        return converter.toDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getByUsername(String username) {
        Employee employee = employeeRepository.findEmployeeByUsername(username)
                .orElseThrow(() -> {
                    throw new NoSuchRecordException
                            (String.format("Employee with username=%s not found", username));
                });
        return converter.toDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByUsername(employeeDTO.getUsername())) {
            throw new DuplicateRecordException
                    (String.format("Employee with username=%s already exists", employeeDTO.getUsername()));
        }
        return converter.toDTO(employeeRepository.save(converter.toEntity(employeeDTO)));
    }

    @Override
    @Transactional
    public List<EmployeeDTO> saveAll(List<EmployeeDTO> employeeDTOs) {
        List<EmployeeDTO> saved = new ArrayList<>();
        for (EmployeeDTO employeeDTO : employeeDTOs) {
            saved.add(save(employeeDTO));
        }
        return saved;
    }

    @Override
    @Transactional
    public EmployeeDTO update(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findEmployeeById(employeeDTO.getId())
                .orElseThrow(() -> {
            throw new NoSuchRecordException(String.format("Employee with id=%s not found", employeeDTO.getId()));
        });
        converter.updateEmployeeFields(employeeDTO, employee);
        return converter.toDTO(employee);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new NoSuchRecordException(String.format("Department with id=%s not found for deleting", id));
        }
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return employeeRepository.existsById(id);
    }
}
