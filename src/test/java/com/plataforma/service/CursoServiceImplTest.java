package com.plataforma.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.plataforma.dto.CursoRequest;
import com.plataforma.dto.CursoResponse;
import com.plataforma.model.Curso;
import com.plataforma.repository.CursoRepository;
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

@ExtendWith(MockitoExtension.class)
class CursoServiceImplTest {

    @Mock
    private CursoRepository repository;

    @InjectMocks
    private CursoServiceImpl service;

    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Cloud Native");
        curso.setInstructor("Cristian Valverde");
        curso.setDuracion(40);
        curso.setCosto(150000.0);
    }

    @Test
    void testGetAllCursos() {
        when(repository.findAll()).thenReturn(Arrays.asList(curso));
        List<CursoResponse> result = service.getAllCursos();
        assertEquals(1, result.size());
        assertEquals("Cloud Native", result.get(0).getNombre());
    }

    @Test
    void testGetCursoByIdFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso));
        CursoResponse result = service.getCursoById(1L);
        assertEquals("Cloud Native", result.getNombre());
    }

    @Test
    void testGetCursoByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getCursoById(99L));
    }

    @Test
    void testCreateCurso() {
        CursoRequest request = new CursoRequest();
        request.setNombre("DevOps");
        request.setInstructor("Juan Perez");
        request.setDuracion(30);
        request.setCosto(120000.0);

        Curso saved = new Curso();
        saved.setId(2L);
        saved.setNombre("DevOps");
        saved.setInstructor("Juan Perez");
        saved.setDuracion(30);
        saved.setCosto(120000.0);

        when(repository.save(any(Curso.class))).thenReturn(saved);

        CursoResponse result = service.createCurso(request);
        assertEquals(2L, result.getId());
        assertEquals("DevOps", result.getNombre());
        assertEquals(120000.0, result.getCosto());
    }
}
