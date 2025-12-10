package com.judith.aplicacionweb.proyectooptica.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Prescripcion {
    private Long idPrescripcion;
    private Long idValoracion; // FK a Valoracion
    private BigDecimal sphOd;
    private BigDecimal cylOd;
    private Integer ejeOd;
    private BigDecimal sphOi;
    private BigDecimal cylOi;
    private Integer ejeOi;
    private BigDecimal adicion;
    private Long tipoPrescripcionId;
    private String observaciones;
    private Timestamp fechaPrescripcion;
    private Long idOptometrista;

    // --- Getters y Setters ---

    public Long getIdPrescripcion() { return idPrescripcion; }
    public void setIdPrescripcion(Long idPrescripcion) { this.idPrescripcion = idPrescripcion; }

    public Long getIdValoracion() { return idValoracion; }
    public void setIdValoracion(Long idValoracion) { this.idValoracion = idValoracion; }

    public BigDecimal getSphOd() { return sphOd; }
    public void setSphOd(BigDecimal sphOd) { this.sphOd = sphOd; }

    public BigDecimal getCylOd() { return cylOd; }
    public void setCylOd(BigDecimal cylOd) { this.cylOd = cylOd; }

    public Integer getEjeOd() { return ejeOd; }
    public void setEjeOd(Integer ejeOd) { this.ejeOd = ejeOd; }

    public BigDecimal getSphOi() { return sphOi; }
    public void setSphOi(BigDecimal sphOi) { this.sphOi = sphOi; }

    public BigDecimal getCylOi() { return cylOi; }
    public void setCylOi(BigDecimal cylOi) { this.cylOi = cylOi; }

    public Integer getEjeOi() { return ejeOi; }
    public void setEjeOi(Integer ejeOi) { this.ejeOi = ejeOi; }

    public BigDecimal getAdicion() { return adicion; }
    public void setAdicion(BigDecimal adicion) { this.adicion = adicion; }

    public Long getTipoPrescripcionId() { return tipoPrescripcionId; }
    public void setTipoPrescripcionId(Long tipoPrescripcionId) { this.tipoPrescripcionId = tipoPrescripcionId; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Timestamp getFechaPrescripcion() { return fechaPrescripcion; }
    public void setFechaPrescripcion(Timestamp fechaPrescripcion) { this.fechaPrescripcion = fechaPrescripcion; }

    public Long getIdOptometrista() { return idOptometrista; }
    public void setIdOptometrista(Long idOptometrista) { this.idOptometrista = idOptometrista; }
}
