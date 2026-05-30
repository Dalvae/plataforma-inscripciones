package com.plataforma.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ResumenItem {

    @Column(name = "curso_id")
    private Long cursoId;

    @Column(name = "curso_nombre")
    private String nombre;

    @Column(name = "instructor")
    private String instructor;

    @Column(name = "costo")
    private Double costo;

    public ResumenItem() {
    }

    public ResumenItem(Long cursoId, String nombre, String instructor, Double costo) {
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
