package com.plataforma.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.plataforma.dto.StudentRequest;
import com.plataforma.dto.StudentResponse;
import com.plataforma.model.Student;
import com.plataforma.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentServiceImpl service;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John");
    }

    private StudentRequest request(String name) {
        StudentRequest r = new StudentRequest();
        r.setName(name);
        return r;
    }

    @Test
    void testGetAllStudents() {
        when(repository.findAll()).thenReturn(Arrays.asList(student));
        List<StudentResponse> result = service.getAllStudents();
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    void testGetStudentByIdFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(student));
        StudentResponse result = service.getStudentById(1L);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
    }

    @Test
    void testGetStudentByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getStudentById(99L));
    }

    @Test
    void testCreateStudent() {
        when(repository.save(any(Student.class))).thenReturn(student);
        StudentResponse result = service.createStudent(request("John"));
        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
    }

    @Test
    void testUpdateStudentExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(student));
        when(repository.save(any(Student.class))).thenReturn(student);
        StudentResponse result = service.updateStudent(1L, request("John"));
        assertEquals("John", result.getName());
        verify(repository).save(student);
    }

    @Test
    void testUpdateStudentNotExists() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.updateStudent(1L, request("John")));
        verify(repository, never()).save(any());
    }

    @Test
    void testDeleteStudent() {
        when(repository.existsById(1L)).thenReturn(true);
        service.deleteStudent(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void testDeleteStudentNotExists() {
        when(repository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> service.deleteStudent(1L));
        verify(repository, never()).deleteById(any());
    }
}
