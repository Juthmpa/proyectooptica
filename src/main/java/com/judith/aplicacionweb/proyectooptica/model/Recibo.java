package com.judith.aplicacionweb.proyectooptica.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Recibo {
    private Long idRecibo;
    private Long idCliente; // FK a clientes
    private Long idUsuario; // FK a usuarios (vendedor)
    private Timestamp fechaEmision;
    private BigDecimal total;
    private BigDecimal abono;
    private BigDecimal saldo;
    private Long idFormaPago; // FK a forma_pago
    private String estado;

    public Recibo( ) {    }

    public Recibo(Long idRecibo, Long idCliente, Long idUsuario, Timestamp fechaEmision,
                  BigDecimal total, BigDecimal abono, BigDecimal saldo, Long idFormaPago,
                  String estado) {
        this.idRecibo = idRecibo;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.abono = abono;
        this.saldo = saldo;
        this.idFormaPago = idFormaPago;
        this.estado = estado;
    }

    public Long getIdRecibo() {
        return idRecibo;
    }

    public void setIdRecibo(Long idRecibo) {
        this.idRecibo = idRecibo;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Timestamp getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Timestamp fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getAbono() {
        return abono;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Long getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(Long idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
