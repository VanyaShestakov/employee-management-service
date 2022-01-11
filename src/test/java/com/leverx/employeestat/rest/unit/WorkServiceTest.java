package com.leverx.employeestat.rest.unit;

import com.leverx.employeestat.rest.dto.WorkDTO;
import com.leverx.employeestat.rest.dto.converter.WorkConverter;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.entity.Work;
import com.leverx.employeestat.rest.entity.WorkId;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import com.leverx.employeestat.rest.repository.WorkRepository;
import com.leverx.employeestat.rest.service.impl.WorkServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private WorkConverter converter;

    @InjectMocks
    private WorkServiceImpl workService;

    private WorkDTO workDTO;
    private Work work;
    private WorkId workId;
    private Employee employee;
    private Project project;

    @BeforeEach
    public void init() {
        workDTO = new WorkDTO();
        work = new Work();
        workId = new WorkId();
        employee = new Employee();
        project = new Project();

        UUID empId = UUID.randomUUID();
        UUID projId = UUID.randomUUID();
        employee.setId(empId);
        project.setId(projId);

        workId.setEmployee(employee);
        workId.setProject(project);

        workDTO.setEmployeeId(empId);
        workDTO.setProjectId(projId);
    }

    @Test
    public void shouldReturnNotEmptyListOfWorksIfRepositoryIsNotEmptyWhenGetAll() {
        final int expectedSize = 3;

        when(workRepository.findAll()).thenReturn(Arrays.asList(work, work, work));
        when(converter.toDTO(work)).thenReturn(workDTO);

        List<WorkDTO> actualList = workService.getAll();
        assertFalse(actualList.isEmpty());
        assertEquals(expectedSize, actualList.size());
        assertSame(actualList.get(0).getClass(), WorkDTO.class);

        verify(workRepository).findAll();
        verify(converter, times(expectedSize)).toDTO(work);
    }

    @Test
    public void shouldReturnEmptyListOfWorksIfRepositoryIsEmptyWhenGetAll() {
        when(workRepository.findAll()).thenReturn(new ArrayList<>());

        List<WorkDTO> actualList = workService.getAll();
        assertTrue(actualList.isEmpty());

        verify(workRepository).findAll();
    }

    @Test
    public void shouldReturnWorkByIdsIfTheyExistInRepositoryWhenGetByIds() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.of(project));
        when(workRepository.findWorkById(workId)).thenReturn(Optional.of(work));
        when(converter.toDTO(work)).thenReturn(workDTO);

        assertSame(WorkDTO.class, workService.getByIds(employee.getId(), project.getId()).getClass());
        assertDoesNotThrow(() -> workService.getByIds(employee.getId(), project.getId()));

        verify(employeeRepository, times(2)).findEmployeeById(employee.getId());
        verify(projectRepository, times(2)).findProjectById(project.getId());
        verify(workRepository, times(2)).findWorkById(workId);
        verify(converter, times(2)).toDTO(work);
    }

    @Test
    public void shouldThrowExceptionIfEmployeeDoesNotExistByEmployeeIdWhenGetByIds() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.ofNullable(null));

        assertThrows(NoSuchRecordException.class, () -> workService.getByIds(employee.getId(), project.getId()));

        verify(employeeRepository).findEmployeeById(employee.getId());
    }

    @Test
    public void shouldThrowExceptionIfProjectDoesNotExistByProjectIdWhenGetByIds() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.ofNullable(null));

        assertThrows(NoSuchRecordException.class, () -> workService.getByIds(employee.getId(), project.getId()));

        verify(employeeRepository).findEmployeeById(employee.getId());
        verify(projectRepository).findProjectById(project.getId());
    }

    @Test
    public void shouldThrowExceptionIfWorkDoesNotExistByWorkIdWhenGetByIds() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.of(project));
        when(workRepository.findWorkById(workId)).thenReturn(Optional.ofNullable(null));

        assertThrows(NoSuchRecordException.class, () -> workService.getByIds(employee.getId(), project.getId()));

        verify(employeeRepository).findEmployeeById(employee.getId());
        verify(projectRepository).findProjectById(project.getId());
        verify(workRepository).findWorkById(workId);
    }

    @Test
    public void shouldUpdateWorkIfItExistsByIdWhenUpdate() {
        final int expectedWorkingHours = 3;
        final LocalDate expectedPositionStartDate = LocalDate.of(2021, 1, 1);
        final LocalDate expectedPositionEndDate = LocalDate.of(2022, 1, 1);

        workDTO.setWorkingHours(3);
        workDTO.setPositionStartDate(expectedPositionStartDate);
        workDTO.setPositionEndDate(expectedPositionEndDate);

        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.of(project));
        when(workRepository.findWorkById(workId)).thenReturn(Optional.of(work));
        when(converter.toDTO(work)).thenReturn(workDTO);

        WorkDTO actual =  workService.update(workDTO);

        assertSame(WorkDTO.class, actual.getClass());
        assertDoesNotThrow(() -> workService.update(workDTO));
        assertEquals(expectedWorkingHours, actual.getWorkingHours());
        assertEquals(expectedPositionStartDate, actual.getPositionStartDate());
        assertEquals(expectedPositionEndDate, actual.getPositionEndDate());

        verify(employeeRepository, times(2)).findEmployeeById(employee.getId());
        verify(projectRepository, times(2)).findProjectById(project.getId());
        verify(workRepository, times(2)).findWorkById(workId);
        verify(converter, times(2)).toDTO(work);
    }

    @Test
    public void shouldThrowExceptionIfEmployeeDoesNotExistByEmployeeIdWhenUpdate() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.ofNullable(null));

        assertThrows(NoSuchRecordException.class, () -> workService.update(workDTO));

        verify(employeeRepository).findEmployeeById(employee.getId());
    }

    @Test
    public void shouldThrowExceptionIfProjectDoesNotExistByProjectIdWhenUpdate() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.ofNullable(null));

        assertThrows(NoSuchRecordException.class, () -> workService.update(workDTO));

        verify(employeeRepository).findEmployeeById(employee.getId());
        verify(projectRepository).findProjectById(project.getId());
    }

    @Test
    public void shouldThrowExceptionIfWorkDoesNotExistByWorkIdWhenUpdate() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.of(project));
        when(workRepository.findWorkById(workId)).thenReturn(Optional.ofNullable(null));

        assertThrows(NoSuchRecordException.class, () -> workService.update(workDTO));

        verify(employeeRepository).findEmployeeById(employee.getId());
        verify(projectRepository).findProjectById(project.getId());
        verify(workRepository).findWorkById(workId);
    }

    @Test
    public void shouldThrowExceptionIfEmployeeDoesNotExistByEmployeeIdWhenDeleteByIds() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.ofNullable(null));

        assertThrows(NoSuchRecordException.class, () -> workService.deleteByIds(employee.getId(), project.getId()));

        verify(employeeRepository).findEmployeeById(employee.getId());
    }

    @Test
    public void shouldThrowExceptionIfProjectDoesNotExistByProjectIdWhenDeleteByIds() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.ofNullable(null));

        assertThrows(NoSuchRecordException.class, () -> workService.deleteByIds(employee.getId(), project.getId()));

        verify(employeeRepository).findEmployeeById(employee.getId());
        verify(projectRepository).findProjectById(project.getId());
    }

    @Test
    public void shouldThrowExceptionIfWorkDoesNotExistByWorkIdWhenDeleteByIds() {
        when(employeeRepository.findEmployeeById(employee.getId())).thenReturn(Optional.of(employee));
        when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.of(project));
        when(workRepository.existsById(workId)).thenReturn(false);

        assertThrows(NoSuchRecordException.class, () -> workService.deleteByIds(employee.getId(), project.getId()));

        verify(employeeRepository).findEmployeeById(employee.getId());
        verify(projectRepository).findProjectById(project.getId());
        verify(workRepository).existsById(workId);
    }

}
