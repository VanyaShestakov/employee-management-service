package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.controller.AvailableEmployeeController;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.dto.converter.EmployeeConverter;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
import com.leverx.employeestat.rest.exception.InvalidPasswordException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.model.ResetPasswordRequest;
import com.leverx.employeestat.rest.service.AuthorizationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter converter;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthorizationServiceImpl(EmployeeRepository employeeRepository, EmployeeConverter converter, PasswordEncoder encoder) {
        this.employeeRepository = employeeRepository;
        this.converter = converter;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public EmployeeDTO registerEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByUsername(employeeDTO.getUsername())) {
            throw  new DuplicateRecordException
                    (String.format("Employee with username=%s already exists", employeeDTO.getUsername()));
        }
        employeeDTO.setPassword(encoder.encode(employeeDTO.getPassword()));
        return converter.toDTO(employeeRepository.save(converter.toEntity(employeeDTO)));
    }

    @Override
    @Transactional
    public List<EmployeeDTO> registerAll(List<EmployeeDTO> employeeDTOs) {
        List<EmployeeDTO> saved = new ArrayList<>();
        for (EmployeeDTO employeeDTO : employeeDTOs) {
            saved.add(registerEmployee(employeeDTO));
        }
        return saved;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        Employee employee = employeeRepository.findEmployeeByUsername(request.getUsername())
                .orElseThrow(() -> new NoSuchRecordException
                        (String.format("Employee with username=%s not found", request.getUsername()))
                );
        if (encoder.matches(request.getOldPassword(), employee.getPassword())) {
            employee.setPassword(encoder.encode(request.getNewPassword()));
        } else {
            throw  new InvalidPasswordException("You entered incorrect old password");
        }
    }
}
