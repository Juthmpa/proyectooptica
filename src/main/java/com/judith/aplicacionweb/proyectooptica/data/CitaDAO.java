package com.judith.aplicacionweb.proyectooptica.data;

import com.judith.aplicacionweb.proyectooptica.model.Cita;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Define las operaciones de acceso a la base de datos para la entidad Cita.
 */
public interface CitaDAO {
    Cita save(Cita cita);
    Optional<Cita> findById(Long idCita);
    List<Cita> findAll(Map<String, Object> filtros);
    void update(Cita cita);
    void delete(Long idCita);

    // Consulta de Regla de Negocio
    boolean existsByOptometristaAndDateTime(Long idOptometrista, LocalDateTime fechaHora);

    // Consulta de Seguridad
    Optional<Long> findRolByIdUsuario(Long idUsuario);
}