package com.leverx.employeestat.rest.unit;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.dto.converter.EmployeeConverter;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
import com.leverx.employeestat.rest.exception.InvalidPasswordException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.model.ResetPasswordRequest;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.service.impl.AuthorizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeConverter converter;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    private EmployeeDTO employeeDTO;
    private Employee employee;
    private ResetPasswordRequest request;

    @BeforeEach
    public void init() {
        employeeDTO = new EmployeeDTO();
        employee = new Employee();
        request = new ResetPasswordRequest();

        employeeDTO.setUsername("username");
        employeeDTO.setPassword("password");

        employee.setUsername("username");
        employee.setPassword("password");

        request.setUsername("username");
        request.setOldPassword("password");
        request.setNewPassword("new");
    }

    @Test
    public void shouldReturnRegisteredEmployeeIfItDoesNotExistByUsernameWhenRegisterEmployee() {
        final String expected = "encoded";

        when(employeeRepository.existsByUsername(employeeDTO.getUsername())).thenReturn(false);
        when(encoder.encode(employeeDTO.getPassword())).thenReturn(expected);
        when(converter.toEntity(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(converter.toDTO(employee)).thenReturn(employeeDTO);

        assertEquals(expected, authorizationService.registerEmployee(employeeDTO).getPassword());

        verify(employeeRepository).existsByUsername(employeeDTO.getUsername());
        verify(encoder).encode("password");
        verify(converter).toEntity(employeeDTO);
        verify(converter).toDTO(employee);
        verify(employeeRepository).save(employee);
    }

    @Test
    public void shouldThrowExceptionIfEmployeeAlreadyExistsByUsernameWhenRegisterEmployee() {
        when(employeeRepository.existsByUsername(employeeDTO.getUsername())).thenReturn(true);

        assertThrows(DuplicateRecordException.class, () -> authorizationService.registerEmployee(employeeDTO));

        verify(employeeRepository).existsByUsername(employeeDTO.getUsername());
    }

    @Test
    public void shouldResetPasswordIfEmployeeExistsByUsernameAndHasCorrectOldPasswordWhenResetPassword() {
        final String expected = "encoded";
        when(employeeRepository.findEmployeeByUsername(request.getUsername())).thenReturn(Optional.of(employee));
        when(encoder.matches(request.getOldPassword(), "password")).thenReturn(true);
        when(encoder.encode(request.getNewPassword())).thenReturn(expected);
        employeeDTO.setPassword(expected);
        when(converter.toDTO(employee)).thenReturn(employeeDTO);

        assertEquals(expected, authorizationService.resetPassword(request).getPassword());

        verify(employeeRepository).findEmployeeByUsername(request.getUsername());
        verify(encoder).matches(request.getOldPassword(), "password");
        verify(encoder).encode(request.getNewPassword());
        verify(converter).toDTO(employee);
    }

    @Test
    public void shouldThrowExceptionIfEmployeeDoesNotExistsByUsernameWhenResetPassword() {
        when(employeeRepository.findEmployeeByUsername(request.getUsername())).thenReturn(Optional.ofNullable(null));

        assertThrows(NoSuchRecordException.class, () -> authorizationService.resetPassword(request));

        verify(employeeRepository).findEmployeeByUsername(request.getUsername());
    }

    @Test
    public void shouldThrowExceptionIfOldPasswordMismatchesCurrentPasswordWhenResetPassword() {
        when(employeeRepository.findEmployeeByUsername(request.getUsername())).thenReturn(Optional.of(employee));
        when(encoder.matches(request.getOldPassword(), "password")).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> authorizationService.resetPassword(request));

        verify(employeeRepository).findEmployeeByUsername(request.getUsername());
        verify(encoder).matches(request.getOldPassword(), "password");
    }

    @Test
    public void test() {
        final String expectedPassword = "encoded";
        final int expectedAmount = 3;
        when(employeeRepository.existsByUsername(employeeDTO.getUsername())).thenReturn(false);
        when(encoder.encode(employeeDTO.getPassword())).thenReturn(expectedPassword);
        when(converter.toEntity(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(converter.toDTO(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> employees = new ArrayList<>();
        for (int i = 0; i < expectedAmount; i++) {
            employees.add(employeeDTO);
        }

        assertEquals(expectedAmount, authorizationService.registerAll(employees).size());

        verify(employeeRepository, times(expectedAmount)).existsByUsername(employeeDTO.getUsername());
        verify(encoder).encode("password");
        verify(converter, times(expectedAmount)).toEntity(employeeDTO);
        verify(converter, times(expectedAmount)).toDTO(employee);
        verify(employeeRepository, times(expectedAmount)).save(employee);
    }
}
