package com.example.bdget.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.bdget.dto.CursoRequest;
import com.example.bdget.dto.CursoResponse;
import com.example.bdget.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CursoController.class)
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService service;

    @Autowired
    private ObjectMapper mapper;

    private CursoResponse cursoResponse;

    @BeforeEach
    void setUp() {
        cursoResponse = new CursoResponse(1L, "Cloud Native", "Cristian Valverde", 40, 150000.0);
    }

    @Test
    void testGetAllCursos() throws Exception {
        when(service.getAllCursos()).thenReturn(Arrays.asList(cursoResponse));
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(cursoResponse))));
    }

    @Test
    void testGetCursoById() throws Exception {
        when(service.getCursoById(1L)).thenReturn(cursoResponse);
        mockMvc.perform(get("/api/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(cursoResponse)));
    }

    @Test
    void testCreateCurso() throws Exception {
        CursoRequest request = new CursoRequest();
        request.setNombre("DevOps");
        request.setInstructor("Juan Perez");
        request.setDuracion(30);
        request.setCosto(120000.0);

        CursoResponse created = new CursoResponse(2L, "DevOps", "Juan Perez", 30, 120000.0);

        when(service.createCurso(any(CursoRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(created)));
    }

    @Test
    void testCreateCursoConNombreVacio() throws Exception {
        CursoRequest request = new CursoRequest();
        request.setNombre("");
        request.setInstructor("Juan Perez");
        request.setDuracion(30);
        request.setCosto(120000.0);

        mockMvc.perform(post("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
