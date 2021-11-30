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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;

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
        Mockito
                .when(converter.toDTO(project))
                .thenReturn(projectDTO);
        Mockito
                .when(projectRepository.findProjectById(id))
                .thenReturn(Optional.of(project));

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
        Project project = new Project();
        ProjectDTO projectDTO = new ProjectDTO();
        project.setName(name);
        projectDTO.setName(name);
        Mockito
                .when(converter.toDTO(project))
                .thenReturn(projectDTO);
        Mockito
                .when(projectRepository.findProjectByName(name))
                .thenReturn(Optional.of(project));

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
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(name);
        Mockito
                .when(converter.toEntity(projectDTO))
                .thenReturn(project);
        Mockito
                .when(converter.toDTO(project))
                .thenReturn(projectDTO);
        Mockito
                .when(projectRepository.existsByName(name))
                .thenReturn(false);
        Mockito
                .when(projectRepository.save(project))
                .thenReturn(project);
        Assertions.assertEquals(projectService.save(projectDTO), projectDTO);
    }

    @Test
    public void shouldThrowExceptionIfProjectExists() {
        String name = "Project name";
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName(name);
        Mockito
                .when(projectRepository.existsByName(name))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateRecordException.class, () -> projectService.save(projectDTO));
    }

    @Test
    public void shouldReturnUpdatedProjectIfItExistsById() {
        UUID id = UUID.randomUUID();
        Project project = new Project();
        project.setId(id);
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(id);
        Mockito
                .when(converter.toEntity(projectDTO))
                .thenReturn(project);
        Mockito
                .when(converter.toDTO(project))
                .thenReturn(projectDTO);
        Mockito
                .when(projectRepository.existsById(id))
                .thenReturn(true);
        Mockito
                .when(projectRepository.save(project))
                .thenReturn(project);
        Assertions.assertEquals(projectDTO, projectService.update(projectDTO));
    }

    //TODO Rewrite test
    /*
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
    }*/

    @Test
    public void shouldThrowExceptionIfProjectAlreadyExists() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(UUID.randomUUID());
        projectDTO.setName("Project");
        Mockito
                .when(projectRepository.existsById(projectDTO.getId()))
                .thenReturn(false);
        Mockito
                .when(projectRepository.existsByName(projectDTO.getName()))
                .thenReturn(true);
        Assertions.assertThrows(DuplicateRecordException.class,
                () -> projectService.update(projectDTO));
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
