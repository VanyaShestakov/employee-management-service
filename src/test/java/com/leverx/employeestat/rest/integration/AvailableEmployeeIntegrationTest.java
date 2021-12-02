package com.leverx.employeestat.rest.integration;

import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.integration.config.TestConfig;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebInitializer.class})
@WebAppConfiguration
@Transactional
public class AvailableEmployeeIntegrationTest {

    public static final String AVAILABLE_EMPLOYEES_ENDPOINT = "/api/available";
    private static final LocalDate NOW = LocalDate.of(2021, 12, 2);

    private final WebApplicationContext webAppContext;
    private MockMvc mvc;

    @Autowired
    public AvailableEmployeeIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
    }

}
