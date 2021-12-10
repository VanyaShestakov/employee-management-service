package com.leverx.employeestat.rest.unit;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.dto.converter.EmployeeConverter;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        when(converter.toDTO(employee)).thenReturn(employeeDTO);
        when(employeeRepository.findEmployeeById(id)).thenReturn(Optional.of(employee));

        Assertions.assertNotNull(employeeService.getById(id));

        verify(converter).toDTO(employee);
        verify(employeeRepository).findEmployeeById(id);
    }

    @Test
    public void shouldThrowExceptionIfEmployeeDoesNotExistsById() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findEmployeeById(id)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> employeeService.getById(id));

        verify(employeeRepository).findEmployeeById(id);
    }

    @Test
    public void shouldReturnEmployeeIfItExistsByUsername() {
        String name = "employee";
        Employee employee = new Employee();
        employee.setUsername(name);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername(name);

        when(converter.toDTO(employee)).thenReturn(employeeDTO);
        when(employeeRepository.findEmployeeByUsername(name)).thenReturn(Optional.of(employee));

        Assertions.assertNotNull(employeeService.getByUsername(name));

        verify(converter).toDTO(employee);
        verify(employeeRepository).findEmployeeByUsername(name);
    }

    @Test
    public void shouldThrowExceptionIfEmployeeDoesNotExistsByUsername() {
        String name = "employee";
        when(employeeRepository.findEmployeeByUsername(name)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> employeeService.getByUsername(name));

        verify(employeeRepository).findEmployeeByUsername(name);
    }

    @Test
    public void shouldReturnEmptyListIfDbTableIsEmpty() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<Employee>());

        Assertions.assertTrue(employeeService.getAll().isEmpty());

        verify(employeeRepository).findAll();
    }

    @Test
    public void shouldReturnNotEmptyListIfDbTableIsNotEmpty() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(new Employee[] {new Employee(), new Employee()}));

        Assertions.assertFalse(employeeService.getAll().isEmpty());

        verify(employeeRepository).findAll();
    }

    @Test
    public void shouldReturnSavedEmployeeIfEmployeeDoesNotExists() {
        String name = "Employee name";
        Employee employee = new Employee();
        employee.setUsername(name);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername(name);

        when(converter.toEntity(employeeDTO)).thenReturn(employee);
        when(converter.toDTO(employee)).thenReturn(employeeDTO);
        when(employeeRepository.existsByUsername(name)).thenReturn(false);
        when(employeeRepository.save(employee)).thenReturn(employee);

        Assertions.assertEquals(employeeService.save(employeeDTO), employeeDTO);

        verify(converter).toEntity(employeeDTO);
        verify(converter).toDTO(employee);
        verify(employeeRepository).existsByUsername(name);
        verify(employeeRepository).save(employee);
    }

    @Test
    public void shouldThrowExceptionIfEmployeeExists() {
        String name = "Employee name";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername(name);

        when(employeeRepository.existsByUsername(name)).thenReturn(true);

        Assertions.assertThrows(DuplicateRecordException.class, () -> employeeService.save(employeeDTO));

        verify(employeeRepository).existsByUsername(name);
    }

    @Test
    public void shouldReturnUpdatedEmployeeIfItExistsById() {
        UUID id = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setId(id);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(id);

        when(converter.toDTO(employee)).thenReturn(employeeDTO);
        when(employeeRepository.findEmployeeById(employeeDTO.getId())).thenReturn(Optional.of(employee));

        Assertions.assertEquals(employeeDTO, employeeService.update(employeeDTO));

        verify(converter).toDTO(employee);
        verify(employeeRepository).findEmployeeById(employeeDTO.getId());
    }

    @Test
    public void shouldThrowExceptionIfEmployeeNotFound() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(UUID.randomUUID());
        employeeDTO.setUsername("Username");

        when(employeeRepository.findEmployeeById(employeeDTO.getId())).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> employeeService.update(employeeDTO));

        verify(employeeRepository).findEmployeeById(employeeDTO.getId());
    }

    @Test
    public void shouldReturnTrueIfEmployeeExistsById() {
        when(employeeRepository.existsById(any())).thenReturn(true);

        Assertions.assertTrue(employeeService.existsById(UUID.randomUUID()));

        verify(employeeRepository).existsById(any());
    }

    @Test
    public void shouldReturnFalseIfEmployeeExistsById() {
        when(employeeRepository.existsById(any())).thenReturn(false);

        Assertions.assertFalse(employeeService.existsById(UUID.randomUUID()));

        verify(employeeRepository).existsById(any());
    }
}
