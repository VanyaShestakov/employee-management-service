package com.leverx.employeestat.rest.integration;

import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.integration.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebInitializer.class})
@WebAppConfiguration
@Transactional
public class AuthorizationIntegrationTest {

    private static final String REGISTER_ENDPOINT = "/api/register";
    private static final String RESET_ENDPOINT = "/api/reset-password";

    private final WebApplicationContext webAppContext;

    private MockMvc mvc;

    @Autowired
    public AuthorizationIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }
}
