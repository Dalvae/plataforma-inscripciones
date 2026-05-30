package com.plataforma.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import com.plataforma.dto.StudentRequest;
import com.plataforma.dto.StudentResponse;
import com.plataforma.service.StudentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @Autowired
    private ObjectMapper mapper;

    private StudentResponse response;

    @BeforeEach
    void setUp() {
        response = new StudentResponse(1L, "John");
    }

    @Test
    void testGetAllStudents() throws Exception {
        when(service.getAllStudents()).thenReturn(Arrays.asList(response));
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Arrays.asList(response))));
    }

    @Test
    void testGetStudentById() throws Exception {
        when(service.getStudentById(1L)).thenReturn(response);
        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void testCreateStudent() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setName("John");
        when(service.createStudent(any(StudentRequest.class))).thenReturn(response);
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void testCreateStudentConNombreInvalido() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setName("");
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateStudent() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setName("John");
        when(service.updateStudent(eq(1L), any(StudentRequest.class))).thenReturn(response);
        mockMvc.perform(put("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
        verify(service).deleteStudent(1L);
    }
}
