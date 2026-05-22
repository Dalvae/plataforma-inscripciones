package com.example.bdget.service;

import com.example.bdget.dto.InscripcionRequest;
import com.example.bdget.dto.InscripcionResponse;

public interface InscripcionService {
    InscripcionResponse inscribir(InscripcionRequest request);
}
