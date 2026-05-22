package com.example.bdget.service;

import com.example.bdget.dto.InscripcionRequest;
import com.example.bdget.dto.InscripcionResponse;
import com.example.bdget.dto.InscripcionResponse.CursoResumen;
import com.example.bdget.model.Curso;
import com.example.bdget.model.Inscripcion;
import com.example.bdget.repository.CursoRepository;
import com.example.bdget.repository.InscripcionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Override
    @Transactional
    public InscripcionResponse inscribir(InscripcionRequest request) {
        List<Curso> cursos = new ArrayList<>();
        List<CursoResumen> resumenCursos = new ArrayList<>();
        double total = 0.0;

        for (Long cursoId : request.getCursoIds()) {
            Curso curso = cursoRepository.findById(cursoId)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Curso con id " + cursoId + " no encontrado"));

            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setEstudianteNombre(request.getEstudianteNombre());
            inscripcion.setCurso(curso);
            inscripcion.setFechaInscripcion(LocalDateTime.now());
            inscripcionRepository.save(inscripcion);

            resumenCursos.add(new CursoResumen(
                    curso.getId(),
                    curso.getNombre(),
                    curso.getInstructor(),
                    curso.getCosto()
            ));
            total += curso.getCosto();
        }

        return new InscripcionResponse(
                request.getEstudianteNombre(),
                LocalDateTime.now(),
                resumenCursos,
                total
        );
    }
}
