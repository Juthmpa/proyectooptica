package com.judith.aplicacionweb.proyectooptica.dto;

import com.judith.aplicacionweb.proyectooptica.model.Cita;
import java.time.LocalDateTime;

public class CitaRequestDTO {
    private Long idCita;
    private Long idCliente;
    private Long idOptometrista;
    private LocalDateTime fechaHora;
    private String motivo;
    private Long idEstado;

    // Getters y Setters
    public Long getIdCita() { return idCita; }
    public void setIdCita(Long idCita) { this.idCita = idCita; }
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }
    public Long getIdOptometrista() { return idOptometrista; }
    public void setIdOptometrista(Long idOptometrista) { this.idOptometrista = idOptometrista; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Long getIdEstado() { return idEstado; }
    public void setIdEstado(Long idEstado) { this.idEstado = idEstado; }

    // Método de conversión a Entidad
    public Cita toEntity() {
        Cita cita = new Cita();
        cita.setIdCita(this.idCita);
        cita.setIdCliente(this.idCliente);
        cita.setIdOptometrista(this.idOptometrista);
        cita.setFechaHora(this.fechaHora);
        cita.setMotivo(this.motivo);
        if (this.idEstado != null) {
            cita.setIdEstado(this.idEstado);
        }
        return cita;
    }
}