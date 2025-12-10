package com.judith.aplicacionweb.proyectooptica.model;

import java.sql.Timestamp;
import java.util.List; // Para incluir las prescripciones asociadas

public class ValoracionVisual {
    private Long idValoracion;
    private Long idCliente;
    private Long idCita;
    private Long idOptometrista;
    private Long idTipoCertificado;
    private Long prescripcionId; // Se mantiene, pero la relación la manejaremos con el objeto Prescripcion
    private String observaciones;
    private Timestamp fechaValoracion;

    // Relación 1:1 o 1:N con Prescripciones
    private Prescripcion prescripcion; // Objeto Prescripcion asociado

    // Campo para mostrar nombre en la vista
    private String nombreCliente;
    private String nombreOptometrista;

    // --- Getters y Setters ---
    public Long getIdValoracion() { return idValoracion; }
    public void setIdValoracion(Long idValoracion) { this.idValoracion = idValoracion; }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public Long getIdCita() { return idCita; }
    public void setIdCita(Long idCita) { this.idCita = idCita; }

    public Long getIdOptometrista() { return idOptometrista; }
    public void setIdOptometrista(Long idOptometrista) { this.idOptometrista = idOptometrista; }

    public Long getIdTipoCertificado() { return idTipoCertificado; }
    public void setIdTipoCertificado(Long idTipoCertificado) { this.idTipoCertificado = idTipoCertificado; }

    public Long getPrescripcionId() { return prescripcionId; }
    public void setPrescripcionId(Long prescripcionId) { this.prescripcionId = prescripcionId; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Timestamp getFechaValoracion() { return fechaValoracion; }
    public void setFechaValoracion(Timestamp fechaValoracion) { this.fechaValoracion = fechaValoracion; }

    public Prescripcion getPrescripcion() { return prescripcion; }
    public void setPrescripcion(Prescripcion prescripcion) { this.prescripcion = prescripcion; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNombreOptometrista() { return nombreOptometrista; }
    public void setNombreOptometrista(String nombreOptometrista) { this.nombreOptometrista = nombreOptometrista; }
}
