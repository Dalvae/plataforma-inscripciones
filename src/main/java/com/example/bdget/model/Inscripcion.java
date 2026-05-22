package com.example.bdget.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripcion")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre del estudiante no puede estar vacio")
    @Column(name = "estudiante_nombre")
    private String estudianteNombre;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(name = "fecha_inscripcion")
    private LocalDateTime fechaInscripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
}
