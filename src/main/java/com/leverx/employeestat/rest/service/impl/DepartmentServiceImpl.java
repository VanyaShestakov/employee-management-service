package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.dto.converter.DepartmentConverter;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.DepartmentRepository;
import com.leverx.employeestat.rest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentConverter converter;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentConverter converter) {
        this.departmentRepository = departmentRepository;
        this.converter = converter;
    }

    @Override
    @Transactional
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        if (departmentRepository.existsByName(departmentDTO.getName())) {
            throw new DuplicateRecordException
                    (String.format("Department with name=%s already exists", departmentDTO.getName()));
        }
        return converter.toDTO(departmentRepository.save(converter.toEntity(departmentDTO)));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!departmentRepository.existsById(id)) {
            throw new NoSuchRecordException
                    (String.format("Department with id=%s not found for deleting", id));
        }
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public DepartmentDTO update(DepartmentDTO departmentDTO) {
        if (departmentDTO.getId() != null && departmentRepository.existsById(departmentDTO.getId())) {
            return converter.toDTO(departmentRepository.save(converter.toEntity(departmentDTO)));
        } else if (!departmentRepository.existsByName(departmentDTO.getName())) {
            return converter.toDTO(departmentRepository.save(converter.toEntity(departmentDTO)));
        } else {
            throw new DuplicateRecordException
                    (String.format("Department with name=%s already exists", departmentDTO.getName()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getById(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                    new NoSuchRecordException(String.format("Department with id=%s not found", id))
                );
        return converter.toDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return departmentRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
}
