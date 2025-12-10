package com.judith.aplicacionweb.proyectooptica.model;

import java.time.LocalDate;
import java.util.Set;

/**
 * Clase de modelo para representar un usuario en el sistema.
 */
public class Usuario {
    private Long id;
    private String username;
    private String passwordHash; // NOTA: En una aplicación real, NUNCA debe almacenarse la contraseña, solo su hash.
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private LocalDate fechaCreacion;
    private Boolean activo;
    private Rol rol;


    // Un Set de Strings para facilitar la verificación de permisos en el Filtro
    private Set<String> codigosPermiso;

    // Constructor
    public Usuario() {
    }

    public Usuario(Long id, String username, String passwordHash, String nombreCompleto, String correo,
                   String telefono, LocalDate fechaCreacion, Boolean activo) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;

    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean getActivo() {
        return activo;
    }
    public void setActivo(boolean estado) {
        this.activo = estado;
    }

    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Set<String> getCodigosPermiso() { return codigosPermiso; }

    /**
     * Este es el método que estaba dando error en UsuarioDAO.
     * Se usa para inyectar los códigos de permiso que se obtienen de la BD.
     */
    public void setCodigosPermiso(Set<String> codigosPermiso) {
        this.codigosPermiso = codigosPermiso;
    }
}

