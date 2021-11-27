package com.leverx.employeestat.rest.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.leverx.employeestat.rest.configuration.Config;
import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.controller.DepartmentController;
import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.exceptionhandler.DepartmentControllerAdvice;
import com.leverx.employeestat.rest.integration.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebInitializer.class})
@WebAppConfiguration
@Transactional
public class DepartmentIntegrationTest {

    public static final String DEPARTMENTS_ENDPOINT = "/api/departments";

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
    public void shouldReturnJsonIfGetRequest() throws Exception {
        mvc.perform(get(DEPARTMENTS_ENDPOINT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCorrectJsonIfGetRequestById() throws Exception {
        mvc.perform(get(DEPARTMENTS_ENDPOINT + "/{id}", "319e97aa-6991-4b10-84b3-6b90ab594a77"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("hr"));
    }

    @Test
    public void shouldReturnNotFoundIfRecordNotFoundById() throws Exception {
        String id = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
        mvc.perform(get(DEPARTMENTS_ENDPOINT + "/{id}", id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void shouldReturnOkStatusIfJsonIsCorrect() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Test");

        mvc.perform(post("/api/departments").contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsNotCorrect() throws Exception {
        mvc.perform(post("/api/departments").contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }


    private String toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}
