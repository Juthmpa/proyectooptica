package com.judith.aplicacionweb.proyectooptica.model;

import java.util.Set;

public class Rol {
    private Long idRol;
    private String nombre;
    private String descripcion;
    private Set<Permiso> permisos; // Colección de permisos asociados

    // Getters y Setters
    public Long getIdRol() { return idRol; }
    public void setIdRol(Long idRol) { this.idRol = idRol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public  String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Set<Permiso> getPermisos() { return permisos; }
    public void setPermisos(Set<Permiso> permisos) { this.permisos = permisos; }
}
