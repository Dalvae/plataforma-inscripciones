package com.plataforma.service;

import com.plataforma.dto.InscripcionRequest;
import com.plataforma.dto.InscripcionResponse;
import com.plataforma.dto.InscripcionResponse.CursoResumen;
import com.plataforma.model.Curso;
import com.plataforma.model.Inscripcion;
import com.plataforma.model.Resumen;
import com.plataforma.model.ResumenItem;
import com.plataforma.repository.CursoRepository;
import com.plataforma.repository.InscripcionRepository;
import com.plataforma.repository.ResumenRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InscripcionServiceImpl implements InscripcionService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private ResumenRepository resumenRepository;

    @Override
    @Transactional
    public InscripcionResponse inscribir(InscripcionRequest request) {
        LocalDateTime ahora = LocalDateTime.now();
        List<ResumenItem> items = new ArrayList<>();
        double total = 0.0;

        for (Long cursoId : request.getCursoIds()) {
            Curso curso = cursoRepository.findById(cursoId)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Curso con id " + cursoId + " no encontrado"));

            // Mantener compatibilidad: una fila Inscripcion por curso
            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setEstudianteNombre(request.getEstudianteNombre());
            inscripcion.setCurso(curso);
            inscripcion.setFechaInscripcion(ahora);
            inscripcionRepository.save(inscripcion);

            items.add(new ResumenItem(
                    curso.getId(),
                    curso.getNombre(),
                    curso.getInstructor(),
                    curso.getCosto()));
            total += curso.getCosto();
        }

        // Crear UN Resumen persistido con numero (id) propio
        Resumen resumen = new Resumen();
        resumen.setEstudianteNombre(request.getEstudianteNombre());
        resumen.setFecha(ahora);
        resumen.setItems(items);
        resumen.setTotalAPagar(total);
        Resumen guardado = resumenRepository.save(resumen);

        List<CursoResumen> resumenCursos = items.stream()
                .map(it -> new CursoResumen(it.getCursoId(), it.getNombre(), it.getInstructor(), it.getCosto()))
                .collect(Collectors.toList());

        return new InscripcionResponse(
                guardado.getId(),
                guardado.getEstudianteNombre(),
                guardado.getFecha(),
                resumenCursos,
                guardado.getTotalAPagar());
    }
}
