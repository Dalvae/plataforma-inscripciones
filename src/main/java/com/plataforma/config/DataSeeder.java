package com.plataforma.config;

import com.plataforma.model.Curso;
import com.plataforma.repository.CursoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public void run(String... args) {
        try {
            if (cursoRepository.count() > 0) {
                log.info("Cursos ya existen en BD, saltando seed");
                return;
            }

            log.info("Sembrando cursos iniciales...");

            cursoRepository.save(createCurso("Cloud Native Fundamentals",
                    "Cristian Valverde", 40, 150000.0));
            cursoRepository.save(createCurso("DevOps con Docker y Kubernetes",
                    "María López", 35, 180000.0));
            cursoRepository.save(createCurso("Spring Boot Avanzado",
                    "Juan Pérez", 30, 120000.0));
            cursoRepository.save(createCurso("Arquitectura AWS",
                    "Ana Torres", 25, 200000.0));
            cursoRepository.save(createCurso("Microservicios con Java",
                    "Pedro García", 45, 250000.0));

            log.info("Seed completo: {} cursos insertados", cursoRepository.count());
        } catch (Exception e) {
            log.warn("No se pudo ejecutar el seed (BD no disponible?): {}", e.getMessage());
        }
    }

    private Curso createCurso(String nombre, String instructor,
                               Integer duracion, Double costo) {
        Curso curso = new Curso();
        curso.setNombre(nombre);
        curso.setInstructor(instructor);
        curso.setDuracion(duracion);
        curso.setCosto(costo);
        return curso;
    }
}
