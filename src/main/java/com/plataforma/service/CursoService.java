package com.plataforma.service;

import com.plataforma.dto.CursoRequest;
import com.plataforma.dto.CursoResponse;
import java.util.List;

public interface CursoService {
    List<CursoResponse> getAllCursos();
    CursoResponse getCursoById(Long id);
    CursoResponse createCurso(CursoRequest request);
}
