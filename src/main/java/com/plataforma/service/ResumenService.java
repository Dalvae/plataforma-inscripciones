package com.plataforma.service;

import com.plataforma.dto.ResumenResponse;

public interface ResumenService {

    ResumenResponse getResumen(Long numero);

    /** Genera (en memoria) el PDF del resumen. */
    byte[] generarPdf(Long numero);

    /** Genera y sube el PDF a S3; guarda la archivoKey. Devuelve la key usada. */
    String subirAS3(Long numero);

    /** Descarga el PDF del resumen desde S3. */
    byte[] descargarDeS3(Long numero);

    /** Re-genera y re-sube el PDF a S3 (modificar). Devuelve la key. */
    String actualizarEnS3(Long numero);

    /** Borra el objeto del resumen en S3 y limpia la archivoKey. */
    void borrarDeS3(Long numero);
}
