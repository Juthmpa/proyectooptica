package com.judith.aplicacionweb.proyectooptica.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrdenTrabajo {
    private Long idOrden;
    private String numeroOt;
    private Long idCliente; // FK a clientes
    private Long idRecibo; // FK a recibos (si aplica)
    private Long idTecnico; // FK a usuarios (el que fabrica/repara)
    private Timestamp fechaIngreso;
    private Timestamp fechaEntrega;
    private Long idEstadoOt; // FK a estado_ot
    private Long idTipoOt; // FK a tipo_ot
    private String observaciones;
    private BigDecimal costo;

    public OrdenTrabajo( ) { }

    public OrdenTrabajo(Long idOrden, String numeroOt, Long idCliente, Long idRecibo, Long idTecnico, Timestamp fechaIngreso, Timestamp fechaEntrega,
                        Long idEstadoOt, Long idTipoOt, String observaciones, BigDecimal costo) {
        this.idOrden = idOrden;
        this.numeroOt = numeroOt;
        this.idCliente = idCliente;
        this.idRecibo = idRecibo;
        this.idTecnico = idTecnico;
        this.fechaIngreso = fechaIngreso;
        this.fechaEntrega = fechaEntrega;
        this.idEstadoOt = idEstadoOt;
        this.idTipoOt = idTipoOt;
        this.observaciones = observaciones;
        this.costo = costo;
    }

    // Getters, Setters

    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public String getNumeroOt() {
        return numeroOt;
    }

    public void setNumeroOt(String numeroOt) {
        this.numeroOt = numeroOt;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdRecibo() {
        return idRecibo;
    }

    public void setIdRecibo(Long idRecibo) {
        this.idRecibo = idRecibo;
    }

    public Long getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Long idTecnico) {
        this.idTecnico = idTecnico;
    }

    public Timestamp getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Timestamp fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Timestamp getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Timestamp fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Long getIdEstadoOt() {
        return idEstadoOt;
    }

    public void setIdEstadoOt(Long idEstadoOt) {
        this.idEstadoOt = idEstadoOt;
    }

    public Long getIdTipoOt() {
        return idTipoOt;
    }

    public void setIdTipoOt(Long idTipoOt) {
        this.idTipoOt = idTipoOt;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }
}
