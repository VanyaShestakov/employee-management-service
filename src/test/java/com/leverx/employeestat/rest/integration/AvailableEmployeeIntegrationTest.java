package com.leverx.employeestat.rest.integration;

import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.integration.config.TestConfig;
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
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
    }

    @Test
    public void shouldReturnTwoAvailableEmployeesNow() throws Exception {
        final String firstExpectedId = "c7fa1cd3-b281-47b1-b72f-92b9391fc5c7";
        final String secondExpectedId = "e9a4e12b-1555-48bd-9275-629856bcdb51";
        final String thirdExpectedId = "2174c05c-e885-4521-985d-774b1f05cd37";

        mvc.perform(get(AVAILABLE_EMPLOYEES_ENDPOINT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].id").value(firstExpectedId))
                .andExpect(jsonPath("$[1].id").value(secondExpectedId))
                .andExpect(jsonPath("$[2].id").value(thirdExpectedId));
    }

    @Test
    public void shouldReturnOneAvailableEmployeeWithinMonth() throws Exception {
        final String firstExpectedId = "c7fa1cd3-b281-47b1-b72f-92b9391fc5c7";
        mvc.perform(get(AVAILABLE_EMPLOYEES_ENDPOINT + "/20"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].id").value(firstExpectedId));
    }
}
