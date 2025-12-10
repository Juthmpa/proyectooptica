package com.judith.aplicacionweb.proyectooptica.model;

public class Permiso {
    private Long idPermiso;
    private String codigo;      // Ej: "CREAR_CLIENTE", "GESTION_USUARIOS"
    private String nombre;
    private String descripcion;

    // Constructor

    // Getters y Setters

    public Long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Long idPermiso) {
        this.idPermiso = idPermiso;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
