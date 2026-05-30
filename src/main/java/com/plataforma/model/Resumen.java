package com.plataforma.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Agregado persistente que representa el "resumen de inscripcion".
 * Su id es el NUMERO de resumen, usado como nombre de carpeta en S3.
 */
@Entity
@Table(name = "resumen")
public class Resumen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "estudiante_nombre")
    private String estudianteNombre;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resumen_item", joinColumns = @JoinColumn(name = "resumen_id"))
    private List<ResumenItem> items = new ArrayList<>();

    @Column(name = "total_a_pagar")
    private Double totalAPagar;

    /** Key/ruta del objeto en S3; null hasta que se sube. */
    @Column(name = "archivo_key")
    private String archivoKey;

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

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<ResumenItem> getItems() {
        return items;
    }

    public void setItems(List<ResumenItem> items) {
        this.items = items;
    }

    public Double getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(Double totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public String getArchivoKey() {
        return archivoKey;
    }

    public void setArchivoKey(String archivoKey) {
        this.archivoKey = archivoKey;
    }
}
