package com.plataforma.controller;

import com.plataforma.dto.ResumenResponse;
import com.plataforma.service.ResumenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resumenes")
@CrossOrigin(origins = "*")
@Tag(name = "Resumenes", description = "Generacion del PDF del resumen y gestion del archivo en AWS S3")
public class ResumenController {

    private final ResumenService resumenService;

    public ResumenController(ResumenService resumenService) {
        this.resumenService = resumenService;
    }

    @GetMapping("/{numero}")
    @Operation(summary = "Consultar un resumen de inscripcion por su numero")
    public ResumenResponse getResumen(@PathVariable Long numero) {
        return resumenService.getResumen(numero);
    }

    @GetMapping("/{numero}/archivo")
    @Operation(summary = "Descargar el PDF del resumen (generado al vuelo)")
    public ResponseEntity<byte[]> descargarArchivo(@PathVariable Long numero) {
        byte[] pdf = resumenService.generarPdf(numero);
        return pdfResponse(numero, pdf);
    }

    @PostMapping("/{numero}/s3")
    @Operation(summary = "Generar (si hace falta) el PDF y subirlo a S3 en la carpeta {numero}")
    public ResponseEntity<String> subirAS3(@PathVariable Long numero) {
        String key = resumenService.subirAS3(numero);
        return ResponseEntity.ok("Resumen subido a S3: " + key);
    }

    @GetMapping("/{numero}/s3")
    @Operation(summary = "Descargar el PDF del resumen desde S3")
    public ResponseEntity<byte[]> descargarDeS3(@PathVariable Long numero) {
        byte[] pdf = resumenService.descargarDeS3(numero);
        return pdfResponse(numero, pdf);
    }

    @PutMapping("/{numero}/s3")
    @Operation(summary = "Re-generar y re-subir el PDF a S3 (modificar)")
    public ResponseEntity<String> actualizarEnS3(@PathVariable Long numero) {
        String key = resumenService.actualizarEnS3(numero);
        return ResponseEntity.ok("Resumen actualizado en S3: " + key);
    }

    @DeleteMapping("/{numero}/s3")
    @Operation(summary = "Borrar el objeto del resumen en S3")
    public ResponseEntity<String> borrarDeS3(@PathVariable Long numero) {
        resumenService.borrarDeS3(numero);
        return ResponseEntity.ok("Resumen borrado de S3 para el numero: " + numero);
    }

    private ResponseEntity<byte[]> pdfResponse(Long numero, byte[] pdf) {
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename("resumen-" + numero + ".pdf")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(disposition);
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
