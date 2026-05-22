package com.example.bdget.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.bdget.dto.InscripcionRequest;
import com.example.bdget.dto.InscripcionResponse;
import com.example.bdget.dto.InscripcionResponse.CursoResumen;
import com.example.bdget.service.InscripcionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InscripcionController.class)
class InscripcionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InscripcionService service;

    @Autowired
    private ObjectMapper mapper;

    private InscripcionResponse response;

    @BeforeEach
    void setUp() {
        CursoResumen resumen = new CursoResumen(1L, "Cloud Native", "Cristian Valverde", 150000.0);
        response = new InscripcionResponse(
                "Diego Munoz",
                LocalDateTime.of(2026, 5, 22, 12, 0),
                Arrays.asList(resumen),
                150000.0
        );
    }

    @Test
    void testInscribir() throws Exception {
        InscripcionRequest request = new InscripcionRequest();
        request.setEstudianteNombre("Diego Munoz");
        request.setCursoIds(Arrays.asList(1L));

        when(service.inscribir(any(InscripcionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estudianteNombre").value("Diego Munoz"))
                .andExpect(jsonPath("$.totalAPagar").value(150000.0));
    }

    @Test
    void testInscribirSinCursos() throws Exception {
        InscripcionRequest request = new InscripcionRequest();
        request.setEstudianteNombre("Diego Munoz");
        request.setCursoIds(Arrays.asList());

        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInscribirSinEstudiante() throws Exception {
        InscripcionRequest request = new InscripcionRequest();
        request.setEstudianteNombre("");
        request.setCursoIds(Arrays.asList(1L));

        mockMvc.perform(post("/api/inscripciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
