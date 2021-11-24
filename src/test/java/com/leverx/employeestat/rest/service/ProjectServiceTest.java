package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.entity.Project;
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
        Mockito.when(projectRepository.findAll()).thenReturn(new ArrayList<Project>());
        Assertions.assertTrue(projectService.getAll().isEmpty());
    }

    @Test
    public void shouldReturnNotEmptyListIfDbTableIsNotEmpty() {
        Mockito.when(projectRepository.findAll()).thenReturn(List.of(new Project(), new Project()));
        Assertions.assertFalse(projectService.getAll().isEmpty());
    }
}
