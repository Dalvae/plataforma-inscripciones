package com.plataforma.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.plataforma.dto.ResumenResponse;
import com.plataforma.model.Resumen;
import com.plataforma.model.ResumenItem;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResumenServiceImplTest {

    @Mock
    private com.plataforma.repository.ResumenRepository resumenRepository;

    @Mock
    private ResumenPdfService pdfService;

    @Mock
    private S3StorageService s3StorageService;

    private ResumenServiceImpl service;

    private Resumen resumen;

    @BeforeEach
    void setUp() {
        service = new ResumenServiceImpl(resumenRepository, pdfService, s3StorageService);
        resumen = new Resumen();
        resumen.setId(5L);
        resumen.setEstudianteNombre("Diego Munoz");
        resumen.setFecha(LocalDateTime.of(2026, 5, 22, 12, 0));
        resumen.setItems(Arrays.asList(new ResumenItem(1L, "Cloud Native", "Cristian Valverde", 150000.0)));
        resumen.setTotalAPagar(150000.0);
    }

    @Test
    void getResumenDevuelveDto() {
        when(resumenRepository.findById(5L)).thenReturn(Optional.of(resumen));
        ResumenResponse response = service.getResumen(5L);
        assertEquals(5L, response.getNumero());
        assertEquals("Diego Munoz", response.getEstudianteNombre());
        assertEquals(1, response.getCursos().size());
    }

    @Test
    void getResumenNoExisteLanza404() {
        when(resumenRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getResumen(99L));
    }

    @Test
    void subirAS3UsaCarpetaIgualAlNumeroYGuardaKey() {
        when(resumenRepository.findById(5L)).thenReturn(Optional.of(resumen));
        when(pdfService.generar(resumen)).thenReturn(new byte[]{1, 2, 3});

        String key = service.subirAS3(5L);

        assertEquals("resumenes/resumen-5.pdf", key);
        verify(s3StorageService).subir(eq("resumenes/resumen-5.pdf"), any(byte[].class), eq("application/pdf"));
        assertEquals("resumenes/resumen-5.pdf", resumen.getArchivoKey());
        verify(resumenRepository).save(resumen);
    }

    @Test
    void descargarDeS3UsaArchivoKeyGuardada() {
        resumen.setArchivoKey("resumenes/resumen-5.pdf");
        when(resumenRepository.findById(5L)).thenReturn(Optional.of(resumen));
        when(s3StorageService.descargar("resumenes/resumen-5.pdf")).thenReturn(new byte[]{4, 5});

        byte[] result = service.descargarDeS3(5L);
        assertArrayEquals(new byte[]{4, 5}, result);
    }

    @Test
    void actualizarEnS3ReGeneraYReSube() {
        when(resumenRepository.findById(5L)).thenReturn(Optional.of(resumen));
        when(pdfService.generar(resumen)).thenReturn(new byte[]{1});

        String key = service.actualizarEnS3(5L);
        assertEquals("resumenes/resumen-5.pdf", key);
        verify(s3StorageService).subir(eq("resumenes/resumen-5.pdf"), any(byte[].class), eq("application/pdf"));
    }

    @Test
    void borrarDeS3EliminaObjetoYLimpiaKey() {
        resumen.setArchivoKey("resumenes/resumen-5.pdf");
        when(resumenRepository.findById(5L)).thenReturn(Optional.of(resumen));

        service.borrarDeS3(5L);

        verify(s3StorageService).borrar("resumenes/resumen-5.pdf");
        assertNull(resumen.getArchivoKey());
        verify(resumenRepository).save(resumen);
    }
}
