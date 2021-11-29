package com.leverx.employeestat.rest.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.dto.ProjectDTO;
import com.leverx.employeestat.rest.exceptionhandler.GlobalControllerAdvice;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
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
public class ProjectIntegrationTest {
    public static final String PROJECTS_ENDPOINT = "/api/projects";

    private final WebApplicationContext webAppContext;
    private MockMvc mvc;
    private ProjectDTO projectDTO;

    @Autowired
    public ProjectIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
        String name = "Windows project";
        String begin = "2021-09-11";
        String end = "2021-11-20";
        projectDTO = new ProjectDTO();
        projectDTO.setName(name);
        projectDTO.setBegin(LocalDate.parse(begin));
        projectDTO.setEnd(LocalDate.parse(end));
    }

    @Test
    public void shouldExistsGlobalControllerAdvice() {
        GlobalControllerAdvice advice = this.webAppContext.getBean(GlobalControllerAdvice.class);
        assertNotNull(advice);
    }

    @Test
    public void shouldReturnJsonIfGetRequest() throws Exception {
        mvc.perform(get(PROJECTS_ENDPOINT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCorrectJsonIfGetRequestById() throws Exception {
        String expectedName = "Windows project";
        String expectedBeginDate = "2021-09-11";
        String expectedEndDate = "2021-11-20";
        mvc.perform(get(PROJECTS_ENDPOINT + "/{id}", "a805fe08-33c6-4be6-98a5-15ff95b0a19d"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.begin").value(expectedBeginDate))
                .andExpect((jsonPath("$.end").value(expectedEndDate)));
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsNotCorrectForPosting() throws Exception {
        mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkStatusIfJsonIsCorrectForPosting() throws Exception{
        DepartmentDTO departmentDTO = new DepartmentDTO();
        String expected = "Test";
        departmentDTO.setName(expected);
        mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expected))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsNotCorrectForPutting() throws Exception {
        mvc.perform(put(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkStatusIfJsonWithoutIdIsCorrectForPutting() throws Exception{
        DepartmentDTO departmentDTO = new DepartmentDTO();
        String expected = "Test";
        departmentDTO.setName(expected);
        mvc.perform(put(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expected))
                .andExpect(jsonPath("$.id").isNotEmpty());
        assertNull(departmentDTO.getId());
    }

    @Test
    public void shouldReturnUpdatedDepartmentIfItExistsById() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        String name = "Test";
        String expectedName = "Expected";
        departmentDTO.setName(name);
        MvcResult result = mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isOk())
                .andReturn();

        departmentDTO = toObject(result.getResponse().getContentAsString());
        departmentDTO.setName(expectedName);
        UUID expectedId = departmentDTO.getId();

        mvc.perform(put(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId.toString()))
                .andExpect(jsonPath("$.name").value(expectedName));
    }

    @Test
    public void shouldDeleteDepartmentIfIdIsCorrectAndExists() throws Exception{
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Test");
        MvcResult result = mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isOk())
                .andReturn();
        departmentDTO = toObject(result.getResponse().getContentAsString());
        UUID id = departmentDTO.getId();

        mvc.perform(delete(PROJECTS_ENDPOINT + "/{id}", id.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnMethodNotAllowedIfUUIDIsEmptyForDeleting() throws Exception {
        mvc.perform(delete(PROJECTS_ENDPOINT + "/{id}", ""))
                .andExpect(status().isMethodNotAllowed());
    }


    private String toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    private DepartmentDTO toObject(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, DepartmentDTO.class);
    }
}
