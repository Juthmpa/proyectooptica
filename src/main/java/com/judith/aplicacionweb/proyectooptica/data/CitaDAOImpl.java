package com.judith.aplicacionweb.proyectooptica.data;

import com.judith.aplicacionweb.proyectooptica.model.Cita;
import com.judith.aplicacionweb.proyectooptica.model.EstadoCita;
import com.judith.aplicacionweb.proyectooptica.service.CitaService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementación simulada (Mock) de CitaDAO.
 * En una aplicación real, esta clase usaría JDBC, JPA o Hibernate.
 */
public class CitaDAOImpl implements CitaDAO {

    private final List<Cita> citasDB = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Cita save(Cita cita) {
        cita.setIdCita(idGenerator.getAndIncrement());
        citasDB.add(cita);
        return cita;
    }

    @Override
    public Optional<Cita> findById(Long idCita) {
        return citasDB.stream()
                .filter(c -> c.getIdCita().equals(idCita))
                .findFirst();
    }

    @Override
    public List<Cita> findAll(Map<String, Object> filtros) {
        // Lógica de filtrado simple simulada
        return citasDB.stream()
                .filter(c -> filtros.get("idOptometrista") == null || c.getIdOptometrista().equals(filtros.get("idOptometrista")))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Cita cita) {
        findById(cita.getIdCita()).ifPresent(c -> {
            // Actualiza todos los campos modificables
            c.setFechaHora(cita.getFechaHora());
            c.setMotivo(cita.getMotivo());
            c.setIdOptometrista(cita.getIdOptometrista());
            c.setIdEstado(cita.getIdEstado());
        });
    }

    @Override
    public void delete(Long idCita) {
        citasDB.removeIf(c -> c.getIdCita().equals(idCita));
    }

    @Override
    public boolean existsByOptometristaAndDateTime(Long idOptometrista, LocalDateTime fechaHora) {
        // Simulación: Chequea si ya existe una cita programada para esa hora y optometrista
        return citasDB.stream()
                .anyMatch(c -> c.getIdOptometrista().equals(idOptometrista) &&
                        c.getFechaHora().equals(fechaHora) &&
                        c.getIdEstado() != EstadoCita.ID_CANCELADA);
    }

    @Override
    public Optional<Long> findRolByIdUsuario(Long idUsuario) {
        // Simulación de roles basada en el contexto del proyecto
        // 1=ADMIN, 2=OPTOMETRISTA, 3=VENTAS (Asistente), 4=LABORATORIO, 5=CLIENTE
        if (idUsuario == 1L) return Optional.of(CitaService.ROL_ADMINISTRADOR);
        if (idUsuario == 2L) return Optional.of(CitaService.ROL_OPTICO);
        if (idUsuario == 3L) return Optional.of(CitaService.ROL_ASISTENTE);
        if (idUsuario == 4L) return Optional.of(CitaService.ROL_LABORATORIO);
        if (idUsuario == 5L) return Optional.of(CitaService.ROL_CLIENTE);
        return Optional.empty();
    }
}