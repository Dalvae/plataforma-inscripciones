package com.plataforma.dto;

import java.time.LocalDateTime;
import java.util.List;

public class InscripcionResponse {

    /** Numero de resumen (id del agregado Resumen persistido). */
    private Long numero;
    private String estudianteNombre;
    private LocalDateTime fecha;
    private List<CursoResumen> cursos;
    private Double totalAPagar;

    public InscripcionResponse() {
    }

    public InscripcionResponse(String estudianteNombre, LocalDateTime fecha, List<CursoResumen> cursos, Double totalAPagar) {
        this.estudianteNombre = estudianteNombre;
        this.fecha = fecha;
        this.cursos = cursos;
        this.totalAPagar = totalAPagar;
    }

    public InscripcionResponse(Long numero, String estudianteNombre, LocalDateTime fecha, List<CursoResumen> cursos, Double totalAPagar) {
        this.numero = numero;
        this.estudianteNombre = estudianteNombre;
        this.fecha = fecha;
        this.cursos = cursos;
        this.totalAPagar = totalAPagar;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<CursoResumen> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoResumen> cursos) {
        this.cursos = cursos;
    }

    public Double getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(Double totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public static class CursoResumen {
        private Long cursoId;
        private String nombre;
        private String instructor;
        private Double costo;

        public CursoResumen() {
        }

        public CursoResumen(Long cursoId, String nombre, String instructor, Double costo) {
            this.cursoId = cursoId;
            this.nombre = nombre;
            this.instructor = instructor;
            this.costo = costo;
        }

        public Long getCursoId() {
            return cursoId;
        }

        public void setCursoId(Long cursoId) {
            this.cursoId = cursoId;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getInstructor() {
            return instructor;
        }

        public void setInstructor(String instructor) {
            this.instructor = instructor;
        }

        public Double getCosto() {
            return costo;
        }

        public void setCosto(Double costo) {
            this.costo = costo;
        }
    }
}
