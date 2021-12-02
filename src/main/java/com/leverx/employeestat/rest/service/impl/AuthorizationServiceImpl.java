package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.dto.converter.EmployeeConverter;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
import com.leverx.employeestat.rest.exception.InvalidPasswordException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.security.request.RegistrationRequest;
import com.leverx.employeestat.rest.security.request.ResetPasswordRequest;
import com.leverx.employeestat.rest.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

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

    @Transactional
    public EmployeeDTO registerEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByUsername(employeeDTO.getUsername())) {
            throw new DuplicateRecordException
                    (String.format("Employee with username=%s already exists", employeeDTO.getUsername()));
        }
        employeeDTO.setPassword(encoder.encode(employeeDTO.getPassword()));
        return converter.toDTO(employeeRepository.save(converter.toEntity(employeeDTO)));
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        Employee employee = employeeRepository.findEmployeeByUsername(request.getUsername())
                .orElseThrow(() -> {
                    throw new NoSuchRecordException
                            (String.format("Employee with username=%s not found", request.getUsername()));
                });
        if (encoder.matches(request.getOldPassword(), employee.getPassword())) {
            employee.setPassword(encoder.encode(request.getNewPassword()));
        } else {
            throw new InvalidPasswordException("You entered incorrect old password");
        }
    }
}