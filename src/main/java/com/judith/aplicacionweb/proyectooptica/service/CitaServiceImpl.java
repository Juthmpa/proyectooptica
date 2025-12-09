package com.judith.aplicacionweb.proyectooptica.service;

import com.judith.aplicacionweb.proyectooptica.data.CitaDAO;
import com.judith.aplicacionweb.proyectooptica.data.CitaDAOImpl;
import com.judith.aplicacionweb.proyectooptica.model.Cita;
import com.judith.aplicacionweb.proyectooptica.model.EstadoCita;
import com.judith.aplicacionweb.proyectooptica.exceptions.PermisoDenegadoException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CitaServiceImpl implements CitaService {

    private final CitaDAO citaDAO;

    public CitaServiceImpl() {
        this.citaDAO = new CitaDAOImpl();
    }

    // Método auxiliar para obtener el rol del usuario que solicita la acción
    private Long obtenerRolDelUsuario(Long idUsuario) {
        // En una app real, si el rol no está en sesión, se consulta la DB:
        return citaDAO.findRolByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario solicitante no encontrado o sin rol asignado."));
    }

    @Override
    public Cita crearCita(Cita cita, Long idUsuarioSolicitante) {
        if (cita.getIdCliente() == null || cita.getFechaHora() == null || cita.getIdOptometrista() == null) {
            throw new IllegalArgumentException("Datos obligatorios faltantes (Cliente, Fecha/Hora, Optometrista).");
        }

        // [Regla de Negocio]: Chequear disponibilidad
        if (citaDAO.existsByOptometristaAndDateTime(cita.getIdOptometrista(), cita.getFechaHora())) {
            throw new RuntimeException("El slot de tiempo no está disponible para este optometrista.");
        }

        // [Regla de Permisos]: Todos (Admin, Optico, Asistente, Cliente) pueden crear.

        cita.setIdEstado(EstadoCita.ID_PROGRAMADA);

        return citaDAO.save(cita);
    }

    @Override
    public List<Cita> buscarCitas(Map<String, Object> filtros) {
        // [Regla de Permisos]: Todos los roles operativos y Cliente pueden leer.
        return citaDAO.findAll(filtros);
    }

    @Override
    public Cita actualizarCita(Cita cita, Long idUsuarioSolicitante) {
        Long idRol = obtenerRolDelUsuario(idUsuarioSolicitante);

        // [Restricción]: Técnico de Laboratorio no tiene acceso
        if (idRol.equals(ROL_LABORATORIO)) {
            throw new PermisoDenegadoException("El Técnico de Laboratorio no tiene permiso para actualizar citas.");
        }

        Cita citaExistente = citaDAO.findById(cita.getIdCita())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

        // [Regla de Seguridad]: Si es cliente (ROL_CLIENTE), solo puede modificar sus propias citas.
        if (idRol.equals(ROL_CLIENTE) && !citaExistente.getIdCliente().equals(idUsuarioSolicitante)) {
            throw new PermisoDenegadoException("El cliente solo puede modificar sus propias citas.");
        }

        // Sincronizar estado si no fue enviado en la petición (mantener el existente)
        if (cita.getIdEstado() == null) {
            cita.setIdEstado(citaExistente.getIdEstado());
        }

        citaDAO.update(cita);
        return cita;
    }

    @Override
    public Cita cancelarCita(Long idCita, Long idUsuarioSolicitante) {
        Long idRol = obtenerRolDelUsuario(idUsuarioSolicitante);

        // [Restricción]: Técnico de Laboratorio no puede cancelar.
        if (idRol.equals(ROL_LABORATORIO)) {
            throw new PermisoDenegadoException("El Técnico de Laboratorio no tiene permiso para cancelar citas.");
        }

        Cita cita = citaDAO.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

        // [Regla de Seguridad]: Si es cliente, solo puede cancelar sus propias citas.
        if (idRol.equals(ROL_CLIENTE) && !cita.getIdCliente().equals(idUsuarioSolicitante)) {
            throw new PermisoDenegadoException("El cliente solo puede cancelar sus propias citas.");
        }

        // La cancelación es un "soft delete" (cambio de estado)
        cita.setIdEstado(EstadoCita.ID_CANCELADA);
        citaDAO.update(cita);
        return cita;
    }

    @Override
    public void eliminarCitaPermanente(Long idCita, Long idUsuarioSolicitante) {
        Long idRol = obtenerRolDelUsuario(idUsuarioSolicitante);

        // [Regla de Seguridad]: SOLO el Administrador/Gerente (ROL 1) puede eliminar permanentemente (hard delete).
        if (!idRol.equals(ROL_ADMINISTRADOR)) {
            throw new PermisoDenegadoException("Solo el Administrador puede eliminar permanentemente un registro de cita.");
        }

        if (citaDAO.findById(idCita).isEmpty()) {
            throw new RuntimeException("Cita no encontrada.");
        }

        citaDAO.delete(idCita);
    }

    @Override
    public Optional<Cita> buscarPorId(Long idCita) {
        return citaDAO.findById(idCita);
    }
}