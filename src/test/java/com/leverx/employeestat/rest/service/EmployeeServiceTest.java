package com.leverx.employeestat.rest.service;

import com.google.common.annotations.VisibleForTesting;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.dto.converter.EmployeeConverter;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.DuplicateEmployeeException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeConverter converter;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    public void shouldReturnEmployeeIfItExistsById() {
        UUID id = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setId(id);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(id);

        Mockito
                .when(converter.toDTO(employee))
                .thenReturn(employeeDTO);
        Mockito
                .when(employeeRepository.findEmployeeById(id))
                .thenReturn(Optional.of(employee));

        Assertions.assertNotNull(employeeService.getById(id));
    }

    @Test
    public void shouldThrowExceptionIfEmployeeDoesNotExistsById() {
        UUID id = UUID.randomUUID();
        Mockito
                .when(employeeRepository.findEmployeeById(id))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> employeeService.getById(id));
    }

    @Test
    public void shouldReturnEmployeeIfItExistsByUsername() {
        String name = "employee";
        Employee employee = new Employee();
        employee.setUsername(name);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername(name);

        Mockito
                .when(converter.toDTO(employee))
                .thenReturn(employeeDTO);
        Mockito
                .when(employeeRepository.findEmployeeByUsername(name))
                .thenReturn(Optional.of(employee));

        Assertions.assertNotNull(employeeService.getByUsername(name));
    }

    @Test
    public void shouldThrowExceptionIfEmployeeDoesNotExistsByUsername() {
        String name = "employee";
        Mockito
                .when(employeeRepository.findEmployeeByUsername(name))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> employeeService.getByUsername(name));
    }

    @Test
    public void shouldReturnEmptyListIfDbTableIsEmpty() {
        Mockito
                .when(employeeRepository.findAll())
                .thenReturn(new ArrayList<Employee>());
        Assertions.assertTrue(employeeService.getAll().isEmpty());
    }

    @Test
    public void shouldReturnNotEmptyListIfDbTableIsNotEmpty() {
        Mockito
                .when(employeeRepository.findAll())
                .thenReturn(List.of(new Employee(), new Employee()));
        Assertions.assertFalse(employeeService.getAll().isEmpty());
    }

    @Test
    public void shouldReturnSavedEmployeeIfEmployeeDoesNotExists() {
        String name = "Employee name";
        Employee employee = new Employee();
        employee.setUsername(name);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername(name);
        Mockito
                .when(converter.toEntity(employeeDTO))
                .thenReturn(employee);
        Mockito
                .when(converter.toDTO(employee))
                .thenReturn(employeeDTO);
        Mockito
                .when(employeeRepository.existsByUsername(name))
                .thenReturn(false);
        Mockito
                .when(employeeRepository.save(employee))
                .thenReturn(employee);
        Assertions.assertEquals(employeeService.save(employeeDTO), employeeDTO);
    }

    @Test
    public void shouldThrowExceptionIfEmployeeExists() {
        String name = "Employee name";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername(name);
        Mockito
                .when(employeeRepository.existsByUsername(name))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateEmployeeException.class, () -> employeeService.save(employeeDTO));
    }

    @Test
    public void shouldReturnUpdatedEmployeeIfItExistsById() {
        UUID id = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setId(id);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(id);

        Mockito
                .when(converter.toEntity(employeeDTO))
                .thenReturn(employee);
        Mockito
                .when(converter.toDTO(employee))
                .thenReturn(employeeDTO);

        Mockito
                .when(employeeRepository.existsById(id))
                .thenReturn(true);
        Mockito
                .when(employeeRepository.save(employee))
                .thenReturn(employee);
        Assertions.assertEquals(employeeDTO, employeeService.update(employeeDTO));
    }

    // TODO Rewrite test
    /*
    @Test
    public void shouldReturnSavedEmployeeIfItDoesNotExistsByUsername() {
        UUID id  = UUID.randomUUID();
        String name = "Employee name";
        Employee expected = new Employee();
        Employee saved = new Employee();
        saved.setUsername(name);
        expected.setUsername(name);
        expected.setId(id);


        Mockito
                .when(employeeRepository.existsByUsername(name))
                .thenReturn(false);
        Mockito
                .when(employeeRepository.save(saved))
                .thenReturn(expected);

        Employee result = employeeService.update(saved);

        Assertions.assertEquals(expected, result);
        Assertions.assertNotEquals(expected, saved);
        Assertions.assertNotEquals(result, saved);
        Assertions.assertNull(saved.getId());
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(result.getId(), expected.getId());
    }*/

    @Test
    public void shouldThrowExceptionIfEmployeeAlreadyExists() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(UUID.randomUUID());
        employeeDTO.setUsername("Username");
        Mockito
                .when(employeeRepository.existsById(employeeDTO.getId()))
                .thenReturn(false);
        Mockito
                .when(employeeRepository.existsByUsername(employeeDTO.getUsername()))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateEmployeeException.class,
                () -> employeeService.update(employeeDTO));
    }

    @Test
    public void shouldReturnTrueIfEmployeeExistsById() {
        Mockito
                .when(employeeRepository.existsById(any()))
                .thenReturn(true);

        Assertions.assertTrue(employeeService.existsById(UUID.randomUUID()));
    }

    @Test
    public void shouldReturnFalseIfEmployeeExistsById() {
        Mockito
                .when(employeeRepository.existsById(any()))
                .thenReturn(false);

        Assertions.assertFalse(employeeService.existsById(UUID.randomUUID()));
    }
}
