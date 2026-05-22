package com.example.bdget.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CursoRequest {

    @NotBlank(message = "El nombre del curso no puede estar vacio")
    private String nombre;

    @NotBlank(message = "El instructor no puede estar vacio")
    private String instructor;

    @NotNull(message = "La duracion es obligatoria")
    private Integer duracion;

    @NotNull(message = "El costo es obligatorio")
    private Double costo;

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

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }
}
