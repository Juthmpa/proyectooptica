package com.judith.aplicacionweb.proyectooptica.model;

public class Optometrista {
    private Long idOptometrista;
    private String nombres;
    private String apellidos;
    private String especialidad;
    private String telefono;
    private String correo;

    public Optometrista() {}

    public Optometrista(Long idOptometrista, String nombres,
                        String apellidos, String especialidad,
                        String telefono, String correo) {
        this.idOptometrista = idOptometrista;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.correo = correo;
    }

    // Getters y Setters
    public Long getIdOptometrista() { return idOptometrista; }
    public void setIdOptometrista(Long idOptometrista) { this.idOptometrista = idOptometrista; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
