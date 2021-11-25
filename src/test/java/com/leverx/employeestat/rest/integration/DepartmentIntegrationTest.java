package com.leverx.employeestat.rest.integration;

import com.leverx.employeestat.rest.configuration.Config;
import com.leverx.employeestat.rest.configuration.WebInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { Config.class, WebInitializer.class})
@WebAppConfiguration
public class DepartmentIntegrationTest {

    private final WebApplicationContext webAppContext;
    private MockMvc mvc;

    @Autowired
    public DepartmentIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
    }

    @Test
    public void test() throws Exception {
        //mvc.perform(get("/api/departments")).andExpect((content().contentType("application/json;charset=UTF-8")));
    }
}
