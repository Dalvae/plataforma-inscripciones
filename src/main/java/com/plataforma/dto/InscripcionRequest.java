package com.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class InscripcionRequest {

    @NotBlank(message = "El nombre del estudiante no puede estar vacio")
    private String estudianteNombre;

    @NotEmpty(message = "Debe seleccionar al menos un curso")
    private List<Long> cursoIds;

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public List<Long> getCursoIds() {
        return cursoIds;
    }

    public void setCursoIds(List<Long> cursoIds) {
        this.cursoIds = cursoIds;
    }
}
