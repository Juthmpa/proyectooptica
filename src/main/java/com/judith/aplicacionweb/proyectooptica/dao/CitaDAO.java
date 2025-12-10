package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.Cita;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {
    private static final String INSERT_CITA =
            "INSERT INTO citas (id_cliente, id_optometrista, fecha_hora, motivo, id_estado) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_CITA_BY_ID =
            "SELECT * FROM citas WHERE id_cita = ?";
    private static final String SELECT_ALL_CITAS =
            "SELECT * FROM citas ORDER BY fecha_hora DESC";
    private static final String UPDATE_CITA =
            "UPDATE citas SET id_cliente=?, id_optometrista=?, fecha_hora=?, motivo=?, id_estado=? WHERE id_cita=?";
    private static final String UPDATE_ESTADO =
            "UPDATE citas SET id_estado = ? WHERE id_cita = ?";

    private Cita mapearCita(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setIdCita(rs.getLong("id_cita"));
        cita.setIdCliente(rs.getLong("id_cliente"));
        cita.setIdOptometrista(rs.getLong("id_optometrista"));
        cita.setFechaHora(rs.getTimestamp("fecha_hora"));
        cita.setMotivo(rs.getString("motivo"));
        cita.setIdEstado(rs.getLong("id_estado"));
        cita.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return cita;
    }

    // 1. Insertar
    public Long insertar(Cita cita) throws SQLException {
        Long idGenerado = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(INSERT_CITA, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, cita.getIdCliente());
            ps.setLong(2, cita.getIdOptometrista());
            ps.setTimestamp(3, cita.getFechaHora());
            ps.setString(4, cita.getMotivo());
            ps.setLong(5, cita.getIdEstado());
            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) { idGenerado = rs.getLong(1); }
            }
        } finally { ConexionBD.close(conn, ps, rs); }
        return idGenerado;
    }

    // 2. Listar
    public List<Cita> listarTodos() {
        List<Cita> citas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_ALL_CITAS);
            rs = ps.executeQuery();
            while (rs.next()) { citas.add(mapearCita(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return citas;
    }

    // 3. Obtener por ID
    public Cita obtenerPorId(Long id) {
        Cita cita = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_CITA_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) { cita = mapearCita(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return cita;
    }

    // 4. Actualizar
    public boolean actualizar(Cita cita) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_CITA);
            ps.setLong(1, cita.getIdCliente());
            ps.setLong(2, cita.getIdOptometrista());
            ps.setTimestamp(3, cita.getFechaHora());
            ps.setString(4, cita.getMotivo());
            ps.setLong(5, cita.getIdEstado());
            ps.setLong(6, cita.getIdCita());
            return ps.executeUpdate() > 0;
        } finally { ConexionBD.close(conn, ps, null); }
    }

    // 5. Cambiar Estado
    public boolean cambiarEstado(Long idCita, Long nuevoEstadoId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_ESTADO);
            ps.setLong(1, nuevoEstadoId);
            ps.setLong(2, idCita);
            return ps.executeUpdate() > 0;
        } finally { ConexionBD.close(conn, ps, null); }
    }

    // 6. Eliminar (Físico si no quieres historial, aunque se recomienda cambiar estado a CANCELADA)
    public boolean eliminar(Long idCita) throws SQLException {
        // Mejor usar cambiarEstado(idCita, ID_ESTADO_CANCELADO)
        throw new UnsupportedOperationException("Usar cambiarEstado(ID_CANCELADO) en lugar de eliminar físico.");
    }
}
