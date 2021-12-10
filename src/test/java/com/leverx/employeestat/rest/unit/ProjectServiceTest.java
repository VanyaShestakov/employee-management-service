package com.leverx.employeestat.rest.unit;

import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.dto.converter.ProjectConverter;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.DuplicateRecordException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import com.leverx.employeestat.rest.service.impl.ProjectServiceImpl;
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
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectConverter converter;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    public void shouldReturnProjectIfItExistsById() {
        UUID id = UUID.randomUUID();
        Project project = new Project();
        ProjectDTO projectDTO = new ProjectDTO();

        when(converter.toDTO(project)).thenReturn(projectDTO);
        when(projectRepository.findProjectById(id)).thenReturn(Optional.of(project));

        Assertions.assertNotNull(projectService.getById(id));

        verify(converter).toDTO(project);
        verify(projectRepository).findProjectById(id);
    }

    @Test
    public void shouldThrowExceptionIfProjectDoesNotExistsById() {
        UUID id = UUID.randomUUID();
        when(projectRepository.findProjectById(id)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> projectService.getById(id));

        verify(projectRepository).findProjectById(id);
    }

    @Test
    public void shouldReturnProjectIfItExistsByName() {
        String name = "project";
        Project project = new Project();
        ProjectDTO projectDTO = new ProjectDTO();
        project.setName(name);
        projectDTO.setName(name);

        when(converter.toDTO(project)).thenReturn(projectDTO);
        when(projectRepository.findProjectByName(name)).thenReturn(Optional.of(project));

        Assertions.assertNotNull(projectService.getByName(name));

        verify(converter).toDTO(project);
        verify(projectRepository).findProjectByName(name);
    }

    @Test
    public void shouldThrowExceptionIfProjectDoesNotExistsByName() {
        String name = "project";

        when(projectRepository.findProjectByName(name)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> projectService.getByName(name));

        verify(projectRepository).findProjectByName(name);
    }

    @Test
    public void shouldReturnEmptyListIfDbTableIsEmpty() {
        when(projectRepository.findAll()).thenReturn(new ArrayList<Project>());

        Assertions.assertTrue(projectService.getAll().isEmpty());

        verify(projectRepository).findAll();
    }

    @Test
    public void shouldReturnNotEmptyListIfDbTableIsNotEmpty() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList( new Project[] {new Project(), new Project()}));

        Assertions.assertFalse(projectService.getAll().isEmpty());

        verify(projectRepository).findAll();
    }

    @Test
    public void shouldReturnSavedProjectIfProjectDoesNotExists() {
        String name = "Project name";
        Project project = new Project();
        project.setName(name);
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(name);

        when(converter.toEntity(projectDTO)).thenReturn(project);
        when(converter.toDTO(project)).thenReturn(projectDTO);
        when(projectRepository.existsByName(name)).thenReturn(false);
        when(projectRepository.save(project)).thenReturn(project);

        Assertions.assertEquals(projectService.save(projectDTO), projectDTO);

        verify(converter).toEntity(projectDTO);
        verify(converter).toDTO(project);
        verify(projectRepository).existsByName(name);
        verify(projectRepository).save(project);
    }

    @Test
    public void shouldThrowExceptionIfProjectExists() {
        String name = "Project name";
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(name);

        when(projectRepository.existsByName(name)).thenReturn(true);

        Assertions.assertThrows(DuplicateRecordException.class, () -> projectService.save(projectDTO));

        verify(projectRepository).existsByName(name);
    }

    @Test
    public void shouldReturnUpdatedProjectIfItExistsById() {
        UUID id = UUID.randomUUID();
        Project project = new Project();
        project.setId(id);
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(id);

        when(converter.toEntity(projectDTO)).thenReturn(project);
        when(converter.toDTO(project)).thenReturn(projectDTO);
        when(projectRepository.existsById(id)).thenReturn(true);
        when(projectRepository.save(project)).thenReturn(project);

        Assertions.assertEquals(projectDTO, projectService.update(projectDTO));

        verify(converter).toEntity(projectDTO);
        verify(converter).toDTO(project);
        verify(projectRepository).existsById(id);
        verify(projectRepository).save(project);
    }

    @Test
    public void shouldThrowExceptionIfProjectAlreadyExists() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(UUID.randomUUID());
        projectDTO.setName("Project");

        when(projectRepository.existsById(projectDTO.getId())).thenReturn(false);
        when(projectRepository.existsByName(projectDTO.getName())).thenReturn(true);

        Assertions.assertThrows(DuplicateRecordException.class, () -> projectService.update(projectDTO));

        verify(projectRepository).existsById(projectDTO.getId());
        verify(projectRepository).existsByName(projectDTO.getName());
    }

    @Test
    public void shouldReturnTrueIfProjectExistsById() {
        when(projectRepository.existsById(any())).thenReturn(true);

        Assertions.assertTrue(projectService.existsById(UUID.randomUUID()));

        verify(projectRepository).existsById(any());
    }

    @Test
    public void shouldReturnFalseIfProjectExistsById() {
        when(projectRepository.existsById(any())).thenReturn(false);

        Assertions.assertFalse(projectService.existsById(UUID.randomUUID()));

        verify(projectRepository).existsById(any());
    }

    @Test
    public void shouldReturnTrueIfProjectExistsByName() {
        String name = "Project name";
        when(projectRepository.existsByName(any())).thenReturn(true);

        Assertions.assertTrue(projectService.existsByName(name));

        verify(projectRepository).existsByName(any());
    }

    @Test
    public void shouldReturnFalseIfProjectExistsByName() {
        String name = "Project name";
        when(projectRepository.existsByName(any())).thenReturn(false);

        Assertions.assertFalse(projectService.existsByName(name));

        verify(projectRepository).existsByName(any());
    }
}
