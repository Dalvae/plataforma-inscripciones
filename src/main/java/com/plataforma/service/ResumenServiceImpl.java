package com.plataforma.service;

import com.plataforma.dto.ResumenResponse;
import com.plataforma.dto.ResumenResponse.CursoItem;
import com.plataforma.model.Resumen;
import com.plataforma.repository.ResumenRepository;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResumenServiceImpl implements ResumenService {

    private static final String CONTENT_TYPE_PDF = "application/pdf";

    private final ResumenRepository resumenRepository;
    private final ResumenPdfService pdfService;
    private final S3StorageService s3StorageService;

    public ResumenServiceImpl(ResumenRepository resumenRepository,
                              ResumenPdfService pdfService,
                              S3StorageService s3StorageService) {
        this.resumenRepository = resumenRepository;
        this.pdfService = pdfService;
        this.s3StorageService = s3StorageService;
    }

    private Resumen buscar(Long numero) {
        return resumenRepository.findById(numero)
                .orElseThrow(() -> new NoSuchElementException("Resumen con numero " + numero + " no encontrado"));
    }

    /** Todos los resumenes en una sola carpeta: resumenes/resumen-{numero}.pdf */
    private String buildKey(Long numero) {
        return "resumenes/resumen-" + numero + ".pdf";
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenResponse getResumen(Long numero) {
        Resumen resumen = buscar(numero);
        return toResponse(resumen);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generarPdf(Long numero) {
        return pdfService.generar(buscar(numero));
    }

    @Override
    @Transactional
    public String subirAS3(Long numero) {
        Resumen resumen = buscar(numero);
        String key = buildKey(numero);
        byte[] pdf = pdfService.generar(resumen);
        s3StorageService.subir(key, pdf, CONTENT_TYPE_PDF);
        resumen.setArchivoKey(key);
        resumenRepository.save(resumen);
        return key;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] descargarDeS3(Long numero) {
        Resumen resumen = buscar(numero);
        String key = resumen.getArchivoKey() != null ? resumen.getArchivoKey() : buildKey(numero);
        return s3StorageService.descargar(key);
    }

    @Override
    @Transactional
    public String actualizarEnS3(Long numero) {
        // Modificar = re-generar y re-subir (sobrescribe el objeto)
        return subirAS3(numero);
    }

    @Override
    @Transactional
    public void borrarDeS3(Long numero) {
        Resumen resumen = buscar(numero);
        String key = resumen.getArchivoKey() != null ? resumen.getArchivoKey() : buildKey(numero);
        s3StorageService.borrar(key);
        resumen.setArchivoKey(null);
        resumenRepository.save(resumen);
    }

    private ResumenResponse toResponse(Resumen resumen) {
        return new ResumenResponse(
                resumen.getId(),
                resumen.getEstudianteNombre(),
                resumen.getFecha(),
                resumen.getItems().stream()
                        .map(it -> new CursoItem(it.getCursoId(), it.getNombre(), it.getInstructor(), it.getCosto()))
                        .collect(Collectors.toList()),
                resumen.getTotalAPagar(),
                resumen.getArchivoKey());
    }
}
