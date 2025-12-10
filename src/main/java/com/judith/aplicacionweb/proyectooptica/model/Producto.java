package com.judith.aplicacionweb.proyectooptica.model;

import java.math.BigDecimal;

public class Producto {
    private Long idProducto;
    private String codigo;
    private String nombre;
    private Long idTipoProducto; // FK a tipo_producto
    private Long idCategoria; // FK a categoria_producto
    private String descripcion;
    private BigDecimal precio; // Usar BigDecimal para NUMERIC(12,2)
    private Integer stock;
    private Long idMaterial; // FK a material_lente
    private Long idTratamiento; // FK a tratamiento_lente
    private Boolean estado;

    public Producto( ) {}

    public Producto(Long idProducto, String codigo, String nombre, Long idTipoProducto,
                    Long idCategoria, String descripcion, BigDecimal precio,
                    Integer stock, Long idMaterial, Long idTratamiento, Boolean estado) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.idTipoProducto = idTipoProducto;
        this.idCategoria = idCategoria;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.idMaterial = idMaterial;
        this.idTratamiento = idTratamiento;
        this.estado = estado;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdTipoProducto() {
        return idTipoProducto;
    }

    public void setIdTipoProducto(Long idTipoProducto) {
        this.idTipoProducto = idTipoProducto;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public Long getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(Long idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
