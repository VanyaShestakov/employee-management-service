package com.leverx.employeestat.rest.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.exceptionhandler.GlobalControllerAdvice;
import com.leverx.employeestat.rest.integration.config.TestConfig;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebInitializer.class})
@WebAppConfiguration
@Transactional
public class EmployeeIntegrationTest {
    public static final String EMPLOYEES_ENDPOINT = "/api/employees";

    private final WebApplicationContext webAppContext;
    private MockMvc mvc;
    private EmployeeDTO employeeDTO;

    @Autowired
    public EmployeeIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
        employeeDTO = new EmployeeDTO();
        String expectedFirstName = "Vova";
        String expectedLastName = "Vovanov";
        String expectedUsername = "Vovich";
        String expectedPosition = "Developer";
        String expectedPassword = "123";
        employeeDTO.setFirstName(expectedFirstName);
        employeeDTO.setLastName(expectedLastName);
        employeeDTO.setUsername(expectedUsername);
        employeeDTO.setPosition(expectedPosition);
        employeeDTO.setPassword(expectedPassword);
    }

    @Test
    public void shouldExistsGlobalControllerAdvice() {
        GlobalControllerAdvice advice = this.webAppContext.getBean(GlobalControllerAdvice.class);
        assertNotNull(advice);
    }

    @Test
    public void shouldReturnJsonIfGetRequest() throws Exception {
        mvc.perform(get(EMPLOYEES_ENDPOINT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCorrectJsonIfGetRequestById() throws Exception {
        String expectedFirstName = "Ivan";
        String expectedLastName = "Shestakov";
        String expectedUsername = "anechka";
        String expectedPosition = "Java Developer";
        mvc.perform(get(EMPLOYEES_ENDPOINT + "/{id}", "ea66da14-8f15-434c-931f-6ff237429904"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(expectedFirstName))
                .andExpect(jsonPath("$.lastName").value(expectedLastName))
                .andExpect(jsonPath("$.username").value(expectedUsername))
                .andExpect(jsonPath("$.position").value(expectedPosition));
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsNotCorrectForPosting() throws Exception {
        mvc.perform(post(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkStatusIfJsonIsCorrectForPosting() throws Exception{
        String expectedFirstName = "Vova";
        String expectedLastName = "Vovanov";
        String expectedUsername = "Vovich";
        String expectedPosition = "Developer";
        String expectedPassword = "123";
        mvc.perform(post(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(expectedFirstName))
                .andExpect(jsonPath("$.lastName").value(expectedLastName))
                .andExpect(jsonPath("$.username").value(expectedUsername))
                .andExpect(jsonPath("$.position").value(expectedPosition))
                .andExpect(jsonPath("$.password").value(expectedPassword));
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsNotCorrectForPutting() throws Exception {
        mvc.perform(put(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkStatusIfJsonWithoutIdIsCorrectForPutting() throws Exception {
        String expectedFirstName = "Vova";
        String expectedLastName = "Vovanov";
        String expectedUsername = "Vovich";
        String expectedPosition = "Developer";
        String expectedPassword = "123";
        mvc.perform(put(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(expectedFirstName))
                .andExpect(jsonPath("$.lastName").value(expectedLastName))
                .andExpect(jsonPath("$.username").value(expectedUsername))
                .andExpect(jsonPath("$.position").value(expectedPosition))
                .andExpect(jsonPath("$.password").value(expectedPassword))
                .andExpect(jsonPath("$.id").isNotEmpty());
        assertNull(employeeDTO.getId());
    }

    @Test
    public void shouldReturnUpdatedEmployeeIfItExistsById() throws Exception {
        MvcResult result = mvc.perform(post(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isOk())
                .andReturn();

        employeeDTO = toObject(result.getResponse().getContentAsString());
        String expectedFirstName = "Sasha";
        employeeDTO.setFirstName(expectedFirstName);
        UUID expectedId = employeeDTO.getId();

        mvc.perform(put(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId.toString()))
                .andExpect(jsonPath("$.firstName").value(expectedFirstName));
    }

    @Test
    public void shouldDeleteEmployeeIfIdIsCorrectAndExists() throws Exception{
        MvcResult result = mvc.perform(post(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isOk())
                .andReturn();
        employeeDTO = toObject(result.getResponse().getContentAsString());
        UUID id = employeeDTO.getId();

        mvc.perform(delete(EMPLOYEES_ENDPOINT + "/{id}", id.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnMethodNotAllowedIfUUIDIsEmptyForDeleting() throws Exception {
        mvc.perform(delete(EMPLOYEES_ENDPOINT + "/{id}", ""))
                .andExpect(status().isMethodNotAllowed());
    }


    private String toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    private EmployeeDTO toObject(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, EmployeeDTO.class);
    }
}
