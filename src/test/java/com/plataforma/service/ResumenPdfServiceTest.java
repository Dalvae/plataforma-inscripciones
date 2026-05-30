package com.plataforma.service;

import static org.junit.jupiter.api.Assertions.*;

import com.plataforma.model.Resumen;
import com.plataforma.model.ResumenItem;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ResumenPdfServiceTest {

    private final ResumenPdfService service = new ResumenPdfService();

    @Test
    void generaPdfNoVacio() {
        Resumen resumen = new Resumen();
        resumen.setId(7L);
        resumen.setEstudianteNombre("Diego Munoz");
        resumen.setFecha(LocalDateTime.of(2026, 5, 22, 12, 0));
        resumen.setItems(Arrays.asList(
                new ResumenItem(1L, "Cloud Native", "Cristian Valverde", 150000.0),
                new ResumenItem(2L, "DevOps", "Juan Perez", 120000.0)));
        resumen.setTotalAPagar(270000.0);

        byte[] pdf = service.generar(resumen);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
        // Cabecera de un archivo PDF
        assertEquals('%', (char) pdf[0]);
        assertEquals('P', (char) pdf[1]);
        assertEquals('D', (char) pdf[2]);
        assertEquals('F', (char) pdf[3]);
    }
}
