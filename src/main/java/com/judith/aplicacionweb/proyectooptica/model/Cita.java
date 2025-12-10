package com.judith.aplicacionweb.proyectooptica.model;

import java.sql.Timestamp;

public class Cita {
    private Long idCita;
    private Long idCliente; // FK a clientes
    private Long idOptometrista; // FK a optometristas
    private Timestamp fechaHora;
    private String motivo;
    private Long idEstado; // FK a estado_cita
    private Timestamp fechaCreacion;

    public Cita( ) { }

    public Cita(Long idCita, Long idCliente, Long idOptometrista,
                Timestamp fechaHora, String motivo, Long idEstado,
                Timestamp fechaCreacion) {
        this.idCita = idCita;
        this.idCliente = idCliente;
        this.idOptometrista = idOptometrista;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.idEstado = idEstado;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters, Setters

    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdOptometrista() {
        return idOptometrista;
    }

    public void setIdOptometrista(Long idOptometrista) {
        this.idOptometrista = idOptometrista;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
