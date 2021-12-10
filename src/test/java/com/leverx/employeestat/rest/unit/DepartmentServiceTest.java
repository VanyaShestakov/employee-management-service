package com.leverx.employeestat.rest.unit;

import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.dto.converter.DepartmentConverter;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentConverter converter;

    @InjectMocks
    private DepartmentServiceImpl departmentService;


    @Test
    public void shouldReturnDepartmentIfItExistsById() {
        UUID id = UUID.randomUUID();
        Department department = new Department();
        DepartmentDTO departmentDTO = new DepartmentDTO();

        when(converter.toDTO(department)).thenReturn(departmentDTO);
        when(departmentRepository.findById(id)).thenReturn(Optional.of(department));

        Assertions.assertNotNull(departmentService.getById(id));

        verify(converter).toDTO(department);
        verify(departmentRepository).findById(id);
    }

    @Test
    public void shouldThrowExceptionIfDepartmentDoesNotExistsById() {
        UUID id = UUID.randomUUID();
        when(departmentRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> departmentService.getById(id));

        verify(departmentRepository).findById(id);
    }

    @Test
    public void shouldReturnEmptyListIfDbTableIsEmpty() {
        when(departmentRepository.findAll()).thenReturn(new ArrayList<Department>());

        Assertions.assertTrue(departmentService.getAll().isEmpty());

        verify(departmentRepository).findAll();
    }

    @Test
    public void shouldReturnNotEmptyListIfDbTableIsNotEmpty() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(new Department[] {new Department(), new Department()}));

        Assertions.assertFalse(departmentService.getAll().isEmpty());

        verify(departmentRepository).findAll();
    }

    @Test
    public void shouldReturnSavedDepartmentIfProjectDoesNotExists() {
        String name = "Department name";
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(name);
        Department department = new Department();
        department.setName(name);

        when(converter.toEntity(departmentDTO)).thenReturn(department);
        when(converter.toDTO(department)).thenReturn(departmentDTO);
        when(departmentRepository.existsByName(name)).thenReturn(false);
        when(departmentRepository.save(department)).thenReturn(department);

        Assertions.assertEquals(departmentService.save(departmentDTO), departmentDTO);

        verify(converter).toEntity(departmentDTO);
        verify(converter).toDTO(department);
        verify(departmentRepository).existsByName(name);
        verify(departmentRepository).save(department);
    }

    @Test
    public void shouldThrowExceptionIfDepartmentExists() {
        String name = "Department name";
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(name);

        when(departmentRepository.existsByName(name)).thenReturn(true);

        Assertions.assertThrows(DuplicateRecordException.class, () -> departmentService.save(departmentDTO));

        verify(departmentRepository).existsByName(name);
    }

    @Test
    public void shouldReturnUpdatedDepartmentIfItExistsById() {
        UUID id = UUID.randomUUID();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(id);
        Department department = new Department();
        department.setId(id);

        when(converter.toEntity(departmentDTO)).thenReturn(department);
        when(converter.toDTO(department)).thenReturn(departmentDTO);
        when(departmentRepository.existsById(id)).thenReturn(true);
        when(departmentRepository.save(department)).thenReturn(department);

        Assertions.assertEquals(departmentDTO, departmentService.update(departmentDTO));

        verify(converter).toEntity(departmentDTO);
        verify(converter).toDTO(department);
        verify(departmentRepository).existsById(id);
        verify(departmentRepository).save(department);
    }

    @Test
    public void shouldThrowExceptionIfDepartmentAlreadyExists() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        UUID id = UUID.randomUUID();
        String name = "Department";

        departmentDTO.setId(id);
        departmentDTO.setName(name);

        when(departmentRepository.existsById(departmentDTO.getId())).thenReturn(false);
        when(departmentRepository.existsByName(departmentDTO.getName())).thenReturn(true);
        Assertions.assertThrows(DuplicateRecordException.class,
                () -> departmentService.update(departmentDTO));

        verify(departmentRepository).existsByName(departmentDTO.getName());
        verify(departmentRepository).existsById(departmentDTO.getId());
    }

    @Test
    public void shouldReturnTrueIfDepartmentExistsById() {
        when(departmentRepository.existsById(any())).thenReturn(true);

        Assertions.assertTrue(departmentService.existsById(UUID.randomUUID()));

        verify(departmentRepository).existsById(any());
    }

    @Test
    public void shouldReturnFalseIfDepartmentExistsById() {
        when(departmentRepository.existsById(any())).thenReturn(false);

        Assertions.assertFalse(departmentService.existsById(UUID.randomUUID()));

        verify(departmentRepository).existsById(any());
    }

    @Test
    public void shouldReturnTrueIfDepartmentExistsByName() {
        String name = "Department name";
        when(departmentRepository.existsByName(any())).thenReturn(true);

        Assertions.assertTrue(departmentService.existsByName(name));

        verify(departmentRepository).existsByName(any());
    }

    @Test
    public void shouldReturnFalseIfDepartmentExistsByName() {
        String name = "Department name";
        when(departmentRepository.existsByName(any())).thenReturn(false);

        Assertions.assertFalse(departmentService.existsByName(name));

        verify(departmentRepository).existsByName(any());
    }
}
