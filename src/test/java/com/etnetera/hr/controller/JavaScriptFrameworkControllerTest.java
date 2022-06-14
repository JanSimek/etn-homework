package com.etnetera.hr.controller;

import com.etnetera.hr.data.HypeLevel;
import com.etnetera.hr.dto.JavaScriptFrameworkDTO;
import com.etnetera.hr.exception.JavascriptFrameworkDuplicateException;
import com.etnetera.hr.exception.JavascriptFrameworkNotFoundException;
import com.etnetera.hr.service.JavaScriptFrameworkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(JavaScriptFrameworkController.class)
public class JavaScriptFrameworkControllerTest {

    @MockBean
    private JavaScriptFrameworkService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private final List<JavaScriptFrameworkDTO> SAMPLE_DATA = List.of(
            new JavaScriptFrameworkDTO(1L, "React", Set.of("1.0"), LocalDate.now(), HypeLevel.HIGH),
            new JavaScriptFrameworkDTO(2L, "Angular", Set.of("14.0", "13.0", "12.0"), LocalDate.of(2025, 1, 1), HypeLevel.MEDIUM),
            new JavaScriptFrameworkDTO(3L, "Vue.js", Set.of("v3.2.37", "v3.2.36"), null, HypeLevel.LOW));

    @Test
    public void testListEmptyFrameworks() throws Exception {
        this.mockMvc.perform(get("/api/v1/frameworks")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testListFrameworks() throws Exception {

        when(service.findAllFrameworks()).thenReturn(SAMPLE_DATA);

        this.mockMvc.perform(get("/api/v1/frameworks")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("React")))
                .andExpect(jsonPath("$[0].version", hasItems("1.0")))
                .andExpect(jsonPath("$[0].deprecationDate", is(LocalDate.now().toString())));
    }

    @Test
    public void testCreateFramework() throws Exception {

        when(service.createFramework(Mockito.<JavaScriptFrameworkDTO>any())).thenReturn(SAMPLE_DATA.get(2));

        this.mockMvc.perform(post("/api/v1/frameworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SAMPLE_DATA.get(2)))).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Vue.js")))
                .andExpect(jsonPath("$.version", hasItems("v3.2.37", "v3.2.36")))
                .andExpect(jsonPath("$.deprecationDate", nullValue()));
    }

    @Test
    public void testAddVersion() throws Exception {

        when(service.createFramework(Mockito.<JavaScriptFrameworkDTO>any())).thenReturn(SAMPLE_DATA.get(2));

        this.mockMvc.perform(post("/api/v1/frameworks/Test/versions/1.2.3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SAMPLE_DATA.get(2)))).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldFailToCreateDuplicateFramework() throws Exception {

        doThrow(new JavascriptFrameworkDuplicateException()).when(service).createFramework(Mockito.<JavaScriptFrameworkDTO>any());

        this.mockMvc.perform(post("/api/v1/frameworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SAMPLE_DATA.get(0))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldFailToUpdateNonExistingFramework() throws Exception {

        doThrow(new JavascriptFrameworkNotFoundException()).when(service).update(anyLong(), Mockito.<JavaScriptFrameworkDTO>any());

        this.mockMvc.perform(put("/api/v1/frameworks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SAMPLE_DATA.get(0))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNoContentAfterUpdateFramework() throws Exception {

        this.mockMvc.perform(put("/api/v1/frameworks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SAMPLE_DATA.get(0))))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldFailToCreateDuplicateVersion() throws Exception {

        doThrow(new JavascriptFrameworkDuplicateException()).when(service).addVersion(anyString(), anyString());

        this.mockMvc.perform(post("/api/v1/frameworks/React/versions/1.0"))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldFailToCreateWhenNameIsTooLong() throws Exception {

        this.mockMvc.perform(post("/api/v1/frameworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new JavaScriptFrameworkDTO(null, "TestNameTooLong123456789013245678901234567890", null, null, null))))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailToCreateWithoutName() throws Exception {

        this.mockMvc.perform(post("/api/v1/frameworks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new JavaScriptFrameworkDTO(null, null, null, null, HypeLevel.INSANE))))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
