package com.leverx.employeestat.rest.dto.converter;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.entity.Project;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.DepartmentRepository;
import com.leverx.employeestat.rest.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class EmployeeConverter {

    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public EmployeeConverter(DepartmentRepository departmentRepository, ProjectRepository projectRepository) {
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
    }

    public Employee toEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setUsername(employeeDTO.getUsername());
        employee.setId(employeeDTO.getId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setPassword(employeeDTO.getPassword());
        employee.setPosition(employeeDTO.getPosition());
        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> {
                        throw new NoSuchRecordException
                                (String.format("Department with id=%s not found", employeeDTO.getDepartmentId()));
                    });
            employee.setDepartment(department);
        }
        if (employeeDTO.getProjectIds() != null) {
            for (UUID id : employeeDTO.getProjectIds()) {
                Project project = projectRepository.findProjectById(id)
                        .orElseThrow(() -> {
                            throw new NoSuchRecordException(String.format("Project with id=%s not found", id));
                        });
            }
        }
        return employee;
    }

    public EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUsername(employee.getUsername());
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setPassword(employee.getPassword());
        employeeDTO.setPosition(employee.getPosition());
        Department department = employee.getDepartment();
        if (department != null) {
            employeeDTO.setDepartmentId(department.getId());
        }
        List<Project> projects = employee.getProjects();
        if (projects != null) {
            for (Project project : projects) {
                employeeDTO.addProjectId(project.getId());
            }
        }
        return employeeDTO;
    }
}
