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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;

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
        Mockito
                .when(converter.toDTO(department))
                .thenReturn(departmentDTO);
        Mockito
                .when(departmentRepository.findById(id))
                .thenReturn(Optional.of(department));

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
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(name);
        Department department = new Department();
        department.setName(name);
        Mockito
                .when(converter.toEntity(departmentDTO))
                .thenReturn(department);
        Mockito
                .when(converter.toDTO(department))
                .thenReturn(departmentDTO);
        Mockito
                .when(departmentRepository.existsByName(name))
                .thenReturn(false);
        Mockito
                .when(departmentRepository.save(department))
                .thenReturn(department);
        Assertions.assertEquals(departmentService.save(departmentDTO), departmentDTO);
    }

    @Test
    public void shouldThrowExceptionIfDepartmentExists() {
        String name = "Department name";
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(name);
        Mockito
                .when(departmentRepository.existsByName(name))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateRecordException.class, () -> departmentService.save(departmentDTO));
    }

    @Test
    public void shouldReturnUpdatedDepartmentIfItExistsById() {
        UUID id = UUID.randomUUID();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(id);
        Department department = new Department();
        department.setId(id);
        Mockito
                .when(converter.toEntity(departmentDTO))
                .thenReturn(department);
        Mockito
                .when(converter.toDTO(department))
                .thenReturn(departmentDTO);
        Mockito
                .when(departmentRepository.existsById(id))
                .thenReturn(true);
        Mockito
                .when(departmentRepository.save(department))
                .thenReturn(department);
        Assertions.assertEquals(departmentDTO, departmentService.update(departmentDTO));
    }

    // TODO Rewrite test
    /*
    @Test
    public void shouldReturnSavedDepartmentIfItDoesNotExistsByName() {
        UUID id  = UUID.randomUUID();
        String name = "Department name";

        DepartmentDTO expected = new DepartmentDTO();
        DepartmentDTO saved = new DepartmentDTO();
        saved.setName(name);
        expected.setName(name);
        expected.setId(id);

        Department savedDepartment = new Department();
        savedDepartment.setName(name);

        Department expectedDepartment = new Department();
        expectedDepartment.setName(name);
        expectedDepartment.setId(id);

        Mockito
                .when(converter.toEntity(saved))
                .thenReturn(savedDepartment);
        Mockito
                .when(converter.toDTO(expectedDepartment))
                .thenReturn(expected);
        Mockito
                .when(departmentRepository.existsByName(name))
                .thenReturn(false);
        Mockito
                .when(departmentRepository.save(savedDepartment))
                .thenReturn(expectedDepartment);

        DepartmentDTO result = departmentService.update(saved);

        Assertions.assertEquals(expected, result);
        Assertions.assertNotEquals(expected, saved);
        Assertions.assertNotEquals(result, saved);
        Assertions.assertNull(saved.getId());
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(result.getId(), expected.getId());
    }
     */

    @Test
    public void shouldThrowExceptionIfDepartmentAlreadyExists() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        UUID id = UUID.randomUUID();
        String name = "Department";

        departmentDTO.setId(id);
        departmentDTO.setName(name);

        Mockito
                .when(departmentRepository.existsById(departmentDTO.getId()))
                .thenReturn(false);
        Mockito
                .when(departmentRepository.existsByName(departmentDTO.getName()))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateRecordException.class,
                () -> departmentService.update(departmentDTO));
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
    }
}
