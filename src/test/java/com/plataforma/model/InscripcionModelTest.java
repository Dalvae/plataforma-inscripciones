package com.plataforma.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class InscripcionModelTest {

    @Test
    void testGettersAndSetters() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Cloud Native");

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setEstudianteNombre("Diego Munoz");
        inscripcion.setCurso(curso);
        inscripcion.setFechaInscripcion(LocalDateTime.of(2026, 5, 22, 12, 0));

        assertEquals(1L, inscripcion.getId());
        assertEquals("Diego Munoz", inscripcion.getEstudianteNombre());
        assertEquals(curso, inscripcion.getCurso());
        assertNotNull(inscripcion.getFechaInscripcion());
    }
}
