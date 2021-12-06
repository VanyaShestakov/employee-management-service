package com.leverx.employeestat.rest.integration;

import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.entity.*;
import com.leverx.employeestat.rest.integration.config.TestConfig;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.repository.RoleRepository;
import com.leverx.employeestat.rest.repository.WorkRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebInitializer.class})
@WebAppConfiguration
@Transactional
public class AvailableEmployeeIntegrationTest {

    public static final String AVAILABLE_EMPLOYEES_ENDPOINT = "/api/employees/available";

    private final WebApplicationContext webAppContext;
    private MockMvc mvc;

    private String firstEmployeeId;
    private String secondEmployeeId;

    @Autowired
    public AvailableEmployeeIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    public void initRepository() {
        EmployeeRepository repository = webAppContext.getBean(EmployeeRepository.class);
        RoleRepository roleRepository = webAppContext.getBean(RoleRepository.class);
        WorkRepository workRepository = webAppContext.getBean(WorkRepository.class);

        Role role = roleRepository.findByName("ROLE_EMPLOYEE").get();

        Employee firstEmployee = new Employee();
        firstEmployee.setFirstName("Ivan");
        firstEmployee.setLastName("Ivanov");
        firstEmployee.setUsername("Ivanov");
        firstEmployee.setPosition("Position");
        firstEmployee.setRole(role);

        Employee secondEmployee = new Employee();
        secondEmployee.setFirstName("Vova");
        secondEmployee.setLastName("Vovanov");
        secondEmployee.setUsername("Vovanov");
        secondEmployee.setPosition("Position");
        secondEmployee.setRole(role);

        Project project = new Project();
        project.setName("Test");

        firstEmployee.addProject(project);
        secondEmployee.addProject(project);

        firstEmployee = repository.save(firstEmployee);
        secondEmployee = repository.save(secondEmployee);

        firstEmployeeId = firstEmployee.getId().toString();
        secondEmployeeId = secondEmployee.getId().toString();

        WorkId firstWorkId = new WorkId();
        firstWorkId.setEmployee(firstEmployee);
        firstWorkId.setProject(project);

        WorkId secondWorkId = new WorkId();
        secondWorkId.setEmployee(secondEmployee);
        secondWorkId.setProject(project);

        Work firstWork = workRepository.findWorkById(firstWorkId).get();
        Work secondWork = workRepository.findWorkById(secondWorkId).get();

        firstWork.setWorkingHours(8);
        firstWork.setPositionStartDate(LocalDate.now().plusDays(20));
        firstWork.setPositionEndDate(LocalDate.now().plusDays(40));

        secondWork.setWorkingHours(8);
        secondWork.setPositionStartDate(LocalDate.now().minusDays(20));
        secondWork.setPositionEndDate(LocalDate.now().minusDays(10));
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
    }

    @Test
    public void shouldReturnTwoAvailableEmployeesNow() throws Exception {
        mvc.perform(get(AVAILABLE_EMPLOYEES_ENDPOINT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].id").value(firstEmployeeId))
                .andExpect(jsonPath("$[1].id").value(secondEmployeeId));
    }

    @Test
    public void shouldReturnOneAvailableEmployeeWithinMonth() throws Exception {
        mvc.perform(get(AVAILABLE_EMPLOYEES_ENDPOINT + "/20"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].id").value(secondEmployeeId));
    }
}
