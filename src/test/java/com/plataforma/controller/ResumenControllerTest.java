package com.plataforma.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.plataforma.service.ResumenService;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ResumenController.class)
class ResumenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResumenService resumenService;

    @Test
    void descargarArchivoDevuelvePdf() throws Exception {
        when(resumenService.generarPdf(5L)).thenReturn(new byte[]{1, 2, 3});
        mockMvc.perform(get("/api/resumenes/5/archivo"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("resumen-5.pdf")));
    }

    @Test
    void subirAS3DevuelveKey() throws Exception {
        when(resumenService.subirAS3(5L)).thenReturn("5/resumen-5.pdf");
        mockMvc.perform(post("/api/resumenes/5/s3"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("5/resumen-5.pdf")));
    }

    @Test
    void descargarDeS3DevuelvePdf() throws Exception {
        when(resumenService.descargarDeS3(5L)).thenReturn(new byte[]{1});
        mockMvc.perform(get("/api/resumenes/5/s3"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"));
    }

    @Test
    void actualizarEnS3Ok() throws Exception {
        when(resumenService.actualizarEnS3(5L)).thenReturn("5/resumen-5.pdf");
        mockMvc.perform(put("/api/resumenes/5/s3"))
                .andExpect(status().isOk());
    }

    @Test
    void borrarDeS3Ok() throws Exception {
        mockMvc.perform(delete("/api/resumenes/5/s3"))
                .andExpect(status().isOk());
        verify(resumenService).borrarDeS3(5L);
    }

    @Test
    void getResumenNoExisteDevuelve404() throws Exception {
        when(resumenService.getResumen(99L))
                .thenThrow(new NoSuchElementException("Resumen con numero 99 no encontrado"));
        mockMvc.perform(get("/api/resumenes/99"))
                .andExpect(status().isNotFound());
    }
}
