package com.judith.aplicacionweb.proyectooptica.model;

public class EstadoCita {
    private Long id;
    private String nombre; // Ej: PROGRAMADA, ATENDIDA, CANCELADA

    // Constantes basadas en el script SQL (INSERT INTO estado_cita)
    public static final Long ID_PROGRAMADA = 1L;
    public static final Long ID_ATENDIDA = 2L;
    public static final Long ID_CANCELADA = 3L;
    public static final Long ID_REAGENDADA = 4L;

    // Constructores
    public EstadoCita() {}
    public EstadoCita(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}