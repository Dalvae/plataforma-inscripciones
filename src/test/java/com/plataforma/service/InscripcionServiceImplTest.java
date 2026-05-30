package com.plataforma.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.plataforma.dto.InscripcionRequest;
import com.plataforma.dto.InscripcionResponse;
import com.plataforma.model.Curso;
import com.plataforma.model.Inscripcion;
import com.plataforma.model.Resumen;
import com.plataforma.repository.CursoRepository;
import com.plataforma.repository.InscripcionRepository;
import com.plataforma.repository.ResumenRepository;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceImplTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private ResumenRepository resumenRepository;

    @InjectMocks
    private InscripcionServiceImpl service;

    private Curso curso1;
    private Curso curso2;

    @BeforeEach
    void setUp() {
        curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Cloud Native");
        curso1.setInstructor("Cristian Valverde");
        curso1.setDuracion(40);
        curso1.setCosto(150000.0);

        curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombre("DevOps");
        curso2.setInstructor("Juan Perez");
        curso2.setDuracion(30);
        curso2.setCosto(120000.0);
    }

    /** Simula la persistencia asignando un id (numero de resumen). */
    private void stubResumenSave() {
        when(resumenRepository.save(any(Resumen.class))).thenAnswer(i -> {
            Resumen r = i.getArgument(0);
            r.setId(100L);
            return r;
        });
    }

    @Test
    void testInscribirUnCurso() {
        InscripcionRequest request = new InscripcionRequest();
        request.setEstudianteNombre("Diego Munoz");
        request.setCursoIds(Arrays.asList(1L));

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso1));
        when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(i -> i.getArgument(0));
        stubResumenSave();

        InscripcionResponse response = service.inscribir(request);

        assertEquals(100L, response.getNumero());
        assertEquals("Diego Munoz", response.getEstudianteNombre());
        assertEquals(1, response.getCursos().size());
        assertEquals(150000.0, response.getTotalAPagar());
    }

    @Test
    void testInscribirMultiplesCursos() {
        InscripcionRequest request = new InscripcionRequest();
        request.setEstudianteNombre("Diego Munoz");
        request.setCursoIds(Arrays.asList(1L, 2L));

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso1));
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso2));
        when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(i -> i.getArgument(0));
        stubResumenSave();

        InscripcionResponse response = service.inscribir(request);

        assertEquals(100L, response.getNumero());
        assertEquals(2, response.getCursos().size());
        assertEquals(270000.0, response.getTotalAPagar());
    }

    @Test
    void testInscribirCursoNoExistente() {
        InscripcionRequest request = new InscripcionRequest();
        request.setEstudianteNombre("Diego Munoz");
        request.setCursoIds(Arrays.asList(99L));

        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.inscribir(request));
    }
}
