package com.example.bdget.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CursoModelTest {

    @Test
    void testGettersAndSetters() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Cloud Native");
        curso.setInstructor("Cristian Valverde");
        curso.setDuracion(40);
        curso.setCosto(150000.0);

        assertEquals(1L, curso.getId());
        assertEquals("Cloud Native", curso.getNombre());
        assertEquals("Cristian Valverde", curso.getInstructor());
        assertEquals(40, curso.getDuracion());
        assertEquals(150000.0, curso.getCosto());
    }
}
