package com.leverx.employeestat.rest.integration;

import com.leverx.employeestat.rest.configuration.WebInitializer;
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

import static com.leverx.employeestat.rest.integration.util.JsonUtils.toJson;
import static com.leverx.employeestat.rest.integration.util.JsonUtils.toObject;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        String name = "project";
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
        String expectedName = "Bank system";
        String expectedBeginDate = "2020-10-08";
        String expectedEndDate = "2020-12-16";

        mvc.perform(get(PROJECTS_ENDPOINT + "/{id}", "7c8697b8-530d-4a00-ba00-3dfec6b1bb79"))
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
    public void shouldReturnBadRequestIfUUIDIsNotCorrectForGetRequestById() throws Exception {
        mvc.perform(get(PROJECTS_ENDPOINT + "/{id}", "incorrect"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void shouldReturnOkStatusIfJsonIsCorrectForPosting() throws Exception{
        String expectedName = "project";
        String expectedBeginDate = "2021-09-11";
        String expectedEndDate = "2021-11-20";

        mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(projectDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.begin").value(expectedBeginDate))
                .andExpect((jsonPath("$.end").value(expectedEndDate)))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void shouldReturnBadRequestIfNameOfProjectAlreadyExistsForPosting() throws Exception {
        mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(projectDTO)))
                .andExpect(status().isCreated());

        mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(projectDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsNotCorrectForPutting() throws Exception {
        mvc.perform(put(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkStatusIfJsonWithoutIdIsCorrectForPutting() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("expected");
        projectDTO.setEnd(LocalDate.of(2021, 5, 11));
        projectDTO.setBegin(LocalDate.of(2021, 2, 11));

        mvc.perform(put(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(projectDTO.getName()))
                .andExpect(jsonPath("$.begin").value(projectDTO.getBegin().toString()))
                .andExpect(jsonPath("$.end").value(projectDTO.getEnd().toString()))
                .andExpect(jsonPath("$.id").isNotEmpty());

        assertNull(projectDTO.getId());
    }

    @Test
    public void shouldReturnBadRequestIfNameOfProjectAlreadyExistsForPutting() throws Exception {
        mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(projectDTO)))
                .andExpect(status().isCreated());

        mvc.perform(put(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(projectDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void shouldReturnUpdatedProjectIfItExistsById() throws Exception {
        MvcResult result = mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(projectDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String expectedName = "New";
        projectDTO = toObject(result.getResponse().getContentAsString(), ProjectDTO.class);
        projectDTO.setName(expectedName);
        UUID expectedId = projectDTO.getId();

        mvc.perform(put(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId.toString()))
                .andExpect(jsonPath("$.name").value(expectedName));
    }

    @Test
    public void shouldDeleteProjectIfIdIsCorrectAndExists() throws Exception {
        MvcResult result = mvc.perform(post(PROJECTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(projectDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        projectDTO = toObject(result.getResponse().getContentAsString(), ProjectDTO.class);
        UUID id = projectDTO.getId();

        mvc.perform(delete(PROJECTS_ENDPOINT + "/{id}", id.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnMethodNotAllowedIfUUIDIsEmptyForDeleting() throws Exception {
        mvc.perform(delete(PROJECTS_ENDPOINT + "/{id}", ""))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldReturnBadRequestIfUUIDIsNotCorrectForDeleteRequestById() throws Exception {
        mvc.perform(delete(PROJECTS_ENDPOINT + "/{id}", "incorrect"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }
}
