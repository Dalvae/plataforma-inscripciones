package com.example.bdget.service;

import com.example.bdget.dto.CursoRequest;
import com.example.bdget.dto.CursoResponse;
import com.example.bdget.model.Curso;
import com.example.bdget.repository.CursoRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public List<CursoResponse> getAllCursos() {
        return cursoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CursoResponse getCursoById(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Curso con id " + id + " no encontrado"));
        return toResponse(curso);
    }

    @Override
    public CursoResponse createCurso(CursoRequest request) {
        Curso curso = new Curso();
        curso.setNombre(request.getNombre());
        curso.setInstructor(request.getInstructor());
        curso.setDuracion(request.getDuracion());
        curso.setCosto(request.getCosto());
        Curso saved = cursoRepository.save(curso);
        return toResponse(saved);
    }

    private CursoResponse toResponse(Curso curso) {
        return new CursoResponse(
                curso.getId(),
                curso.getNombre(),
                curso.getInstructor(),
                curso.getDuracion(),
                curso.getCosto()
        );
    }
}
