package com.example.bdget.service;

import com.example.bdget.dto.CursoRequest;
import com.example.bdget.dto.CursoResponse;
import java.util.List;

public interface CursoService {
    List<CursoResponse> getAllCursos();
    CursoResponse getCursoById(Long id);
    CursoResponse createCurso(CursoRequest request);
}
