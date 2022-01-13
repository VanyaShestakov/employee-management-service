package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.DepartmentRepository;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import com.leverx.employeestat.rest.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EmployeeConverter {

    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public EmployeeConverter(DepartmentRepository departmentRepository,
                             ProjectRepository projectRepository,
                             RoleRepository roleRepository) {
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
        this.roleRepository = roleRepository;
    }

    public Employee toEntity(EmployeeDTO employeeDTO) {
        Employee employee = Employee.builder()
                .username(employeeDTO.getUsername())
                .id(employeeDTO.getId())
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .password(employeeDTO.getPassword())
                .position(employeeDTO.getPosition())
                .build();

        employee.setRole(roleRepository.findByName(employeeDTO.getRole())
                .orElseThrow(() -> new NoSuchRecordException
                        (String.format("Role with name=%s does not exists", employeeDTO.getRole())))
        );

        Optional.ofNullable(employeeDTO.getDepartmentId())
                .ifPresent(id -> {
                    Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                            .orElseThrow(() -> new NoSuchRecordException
                                    (String.format("Department with id=%s not found", employeeDTO.getDepartmentId()))
                            );
                    employee.setDepartment(department);
                });

        Optional.ofNullable(employeeDTO.getProjectIds())
                .ifPresent(projectIds -> projectIds.forEach(id -> {
                    Project project = projectRepository.findProjectById(id)
                            .orElseThrow(() -> new NoSuchRecordException
                                    (String.format("Project with id=%s not found", id))
                            );
                    employee.addProject(project);
                }));

        return employee;
    }

    public void updateEmployeeFields(EmployeeDTO employeeDTO, Employee employee) {
        employee.setUsername(employeeDTO.getUsername());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setPosition(employeeDTO.getPosition());

        employee.setRole(roleRepository.findByName(employeeDTO.getRole())
                .orElseThrow(() -> new NoSuchRecordException
                        ("Role with name=" + employeeDTO.getRole() + " does not exists"))
        );

        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new NoSuchRecordException
                            (String.format("Department with id=%s not found", employeeDTO.getDepartmentId()))
                    );
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        if (employeeDTO.getProjectIds() != null) {
            employee.setProjects(null);
            for (UUID id : employeeDTO.getProjectIds()) {
                Project project = projectRepository.findProjectById(id)
                        .orElseThrow(() -> new NoSuchRecordException
                                (String.format("Project with id=%s not found", id))
                        );
                employee.addProject(project);
            }
        } else {
            employee.setProjects(null);
        }
    }

    public EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .username(employee.getUsername())
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .password(employee.getPassword())
                .position(employee.getPosition())
                .role(employee.getRoleName())
                .build();

        Optional.ofNullable(employee.getDepartment())
                .ifPresent(department -> employeeDTO.setDepartmentId(department.getId()));

        Optional.ofNullable(employee.getProjects())
                .ifPresent((projects -> projects.forEach(project -> employeeDTO.addProjectId(project.getId()))));

        return employeeDTO;
    }
}
