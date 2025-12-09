package com.judith.aplicacionweb.proyectooptica.service;

import com.judith.aplicacionweb.proyectooptica.model.Cita;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CitaService {

    // Constantes de Roles (basadas en el script SQL y contexto)
    Long ROL_ADMINISTRADOR = 1L;   // ADMIN
    Long ROL_OPTICO = 2L;          // OPTOMETRISTA
    Long ROL_ASISTENTE = 3L;       // VENTAS (Asistente/Recepción)
    Long ROL_LABORATORIO = 4L;     // LABORATORIO
    Long ROL_CLIENTE = 5L;         // Cliente (Asumido para el portal)

    Cita crearCita(Cita cita, Long idUsuarioSolicitante);
    List<Cita> buscarCitas(Map<String, Object> filtros);
    Cita actualizarCita(Cita cita, Long idUsuarioSolicitante);

    // Reglas de Permisos
    Cita cancelarCita(Long idCita, Long idUsuarioSolicitante);
    void eliminarCitaPermanente(Long idCita, Long idUsuarioSolicitante); // Borrado físico (Solo ADMIN)

    // Lógica auxiliar
    Optional<Cita> buscarPorId(Long idCita);
}