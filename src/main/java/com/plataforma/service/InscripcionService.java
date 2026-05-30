package com.plataforma.service;

import com.plataforma.dto.InscripcionRequest;
import com.plataforma.dto.InscripcionResponse;

public interface InscripcionService {
    InscripcionResponse inscribir(InscripcionRequest request);
}
