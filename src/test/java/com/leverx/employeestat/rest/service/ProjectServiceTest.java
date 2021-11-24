package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.DuplicateProjectException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    public void shouldReturnProjectIfItExistsById() {
        UUID id = UUID.randomUUID();
        Mockito
                .when(projectRepository.findProjectById(id))
                .thenReturn(Optional.of(new Project()));

        Assertions.assertNotNull(projectService.getById(id));
    }

    @Test
    public void shouldThrowExceptionIfProjectDoesNotExistsById() {
        UUID id = UUID.randomUUID();
        Mockito
                .when(projectRepository.findProjectById(id))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> projectService.getById(id));
    }

    @Test
    public void shouldReturnProjectIfItExistsByName() {
        String name = "project";
        Mockito
                .when(projectRepository.findProjectByName(name))
                .thenReturn(Optional.of(new Project()));

        Assertions.assertNotNull(projectService.getByName(name));
    }

    @Test
    public void shouldThrowExceptionIfProjectDoesNotExistsByName() {
        String name = "project";
        Mockito
                .when(projectRepository.findProjectByName(name))
                .thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(NoSuchRecordException.class, () -> projectService.getByName(name));
    }

    @Test
    public void shouldReturnEmptyListIfDbTableIsEmpty() {
        Mockito
                .when(projectRepository.findAll())
                .thenReturn(new ArrayList<Project>());
        Assertions.assertTrue(projectService.getAll().isEmpty());
    }

    @Test
    public void shouldReturnNotEmptyListIfDbTableIsNotEmpty() {
        Mockito
                .when(projectRepository.findAll())
                .thenReturn(List.of(new Project(), new Project()));
        Assertions.assertFalse(projectService.getAll().isEmpty());
    }

    @Test
    public void shouldReturnSavedProjectIfProjectDoesNotExists() {
        String name = "Project name";
        Project project = new Project();
        project.setName(name);
        Mockito
                .when(projectRepository.existsByName(name))
                .thenReturn(false);
        Mockito
                .when(projectRepository.save(project))
                .thenReturn(project);
        Assertions.assertEquals(projectService.save(project), project);
    }

    @Test
    public void shouldThrowExceptionIfProjectExists() {
        String name = "Project name";
        Project project = new Project();
        project.setName(name);
        Mockito
                .when(projectRepository.existsByName(name))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateProjectException.class, () -> projectService.save(project));
    }

    @Test
    public void shouldReturnUpdatedProjectIfItExistsById() {
        UUID id = UUID.randomUUID();
        Project project = new Project();
        project.setId(id);
        Mockito
                .when(projectRepository.existsById(id))
                .thenReturn(true);
        Mockito
                .when(projectRepository.save(project))
                .thenReturn(project);
        Assertions.assertEquals(project, projectService.update(project));
    }

    @Test
    public void shouldReturnSavedProjectIfItDoesNotExistsByName() {
        UUID id  = UUID.randomUUID();
        String name = "ProjectName";
        Project expected = new Project();
        Project saved = new Project();
        saved.setName(name);
        expected.setName(name);
        expected.setId(id);
        Mockito
                .when(projectRepository.existsById(any()))
                .thenReturn(false);
        Mockito
                .when(projectRepository.existsByName(name))
                .thenReturn(false);
        Mockito
                .when(projectRepository.save(saved))
                .thenReturn(expected);

        Project result = projectService.update(saved);

        Assertions.assertEquals(expected, result);
        Assertions.assertNotEquals(expected, saved);
        Assertions.assertNotEquals(result, saved);
        Assertions.assertNull(saved.getId());
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals(result.getId(), expected.getId());
    }

    @Test
    public void shouldThrowExceptionIfProjectAlreadyExists() {
        Mockito
                .when(projectRepository.existsById(any()))
                .thenReturn(false);
        Mockito
                .when(projectRepository.existsByName(any()))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateProjectException.class,
                () -> projectService.update(new Project()));
    }

    @Test
    public void shouldReturnTrueIfProjectExistsById() {
        Mockito
                .when(projectRepository.existsById(any()))
                .thenReturn(true);

        Assertions.assertTrue(projectService.existsById(UUID.randomUUID()));
    }

    @Test
    public void shouldReturnFalseIfProjectExistsById() {
        Mockito
                .when(projectRepository.existsById(any()))
                .thenReturn(false);

        Assertions.assertFalse(projectService.existsById(UUID.randomUUID()));
    }

    @Test
    public void shouldReturnTrueIfProjectExistsByName() {
        String name = "Project name";
        Mockito
                .when(projectRepository.existsByName(any()))
                .thenReturn(true);

        Assertions.assertTrue(projectService.existsByName(name));
    }

    @Test
    public void shouldReturnFalseIfProjectExistsByName() {
        String name = "Project name";
        Mockito
                .when(projectRepository.existsByName(any()))
                .thenReturn(false);

        Assertions.assertFalse(projectService.existsByName(name));
    }
}
