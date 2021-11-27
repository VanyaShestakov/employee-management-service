package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.exception.DuplicateDepartmentException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.DepartmentRepository;
import com.leverx.employeestat.rest.service.impl.DepartmentServiceImpl;
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
public class DepartmentServiceTest {
/*
    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    public void shouldReturnDepartmentIfItExistsById() {
        UUID id = UUID.randomUUID();
        Mockito
                .when(departmentRepository.findById(id))
                .thenReturn(Optional.of(new Department()));

        Assertions.assertNotNull(departmentService.getById(id));
    }

    @Test
    public void shouldThrowExceptionIfDepartmentDoesNotExistsById() {
        UUID id = UUID.randomUUID();
        Mockito
                .when(departmentRepository.findById(id))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> departmentService.getById(id));
    }

    @Test
    public void shouldReturnEmptyListIfDbTableIsEmpty() {
        Mockito
                .when(departmentRepository.findAll())
                .thenReturn(new ArrayList<Department>());
        Assertions.assertTrue(departmentService.getAll().isEmpty());
    }

    @Test
    public void shouldReturnNotEmptyListIfDbTableIsNotEmpty() {
        Mockito
                .when(departmentRepository.findAll())
                .thenReturn(List.of(new Department(), new Department()));
        Assertions.assertFalse(departmentService.getAll().isEmpty());
    }

    @Test
    public void shouldReturnSavedDepartmentIfProjectDoesNotExists() {
        String name = "Department name";
        Department department = new Department();
        department.setName(name);
        Mockito
                .when(departmentRepository.existsByName(name))
                .thenReturn(false);
        Mockito
                .when(departmentRepository.save(department))
                .thenReturn(department);
        Assertions.assertEquals(departmentService.save(department), department);
    }

    @Test
    public void shouldThrowExceptionIfDepartmentExists() {
        String name = "Department name";
        Department department = new Department();
        department.setName(name);
        Mockito
                .when(departmentRepository.existsByName(name))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateDepartmentException.class, () -> departmentService.save(department));
    }

    @Test
    public void shouldReturnUpdatedDepartmentIfItExistsById() {
        UUID id = UUID.randomUUID();
        Department department = new Department();
        department.setId(id);
        Mockito
                .when(departmentRepository.existsById(id))
                .thenReturn(true);
        Mockito
                .when(departmentRepository.save(department))
                .thenReturn(department);
        Assertions.assertEquals(department, departmentService.update(department));
    }

    @Test
    public void shouldReturnSavedDepartmentIfItDoesNotExistsByName() {
        UUID id  = UUID.randomUUID();
        String name = "Department name";
        Department expected = new Department();
        Department saved = new Department();
        saved.setName(name);
        expected.setName(name);
        expected.setId(id);
//        Mockito
//                .when(departmentRepository.existsById(any()))
//                .thenReturn(false);
        Mockito
                .when(departmentRepository.existsByName(name))
                .thenReturn(false);
        Mockito
                .when(departmentRepository.save(saved))
                .thenReturn(expected);

        Department result = departmentService.update(saved);

        Assertions.assertEquals(expected, result);
        Assertions.assertNotEquals(expected, saved);
        Assertions.assertNotEquals(result, saved);
        Assertions.assertNull(saved.getId());
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(result.getId(), expected.getId());
    }

    @Test
    public void shouldThrowExceptionIfDepartmentAlreadyExists() {
        Mockito
                .when(departmentRepository.existsById(any()))
                .thenReturn(false);
        Mockito
                .when(departmentRepository.existsByName(any()))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateDepartmentException.class,
                () -> departmentService.update(new Department()));
    }

    @Test
    public void shouldReturnTrueIfDepartmentExistsById() {
        Mockito
                .when(departmentRepository.existsById(any()))
                .thenReturn(true);

        Assertions.assertTrue(departmentService.existsById(UUID.randomUUID()));
    }

    @Test
    public void shouldReturnFalseIfDepartmentExistsById() {
        Mockito
                .when(departmentRepository.existsById(any()))
                .thenReturn(false);

        Assertions.assertFalse(departmentService.existsById(UUID.randomUUID()));
    }

    @Test
    public void shouldReturnTrueIfDepartmentExistsByName() {
        String name = "Department name";
        Mockito
                .when(departmentRepository.existsByName(any()))
                .thenReturn(true);

        Assertions.assertTrue(departmentService.existsByName(name));
    }

    @Test
    public void shouldReturnFalseIfDepartmentExistsByName() {
        String name = "Department name";
        Mockito
                .when(departmentRepository.existsByName(any()))
                .thenReturn(false);

        Assertions.assertFalse(departmentService.existsByName(name));
    }*/
}
