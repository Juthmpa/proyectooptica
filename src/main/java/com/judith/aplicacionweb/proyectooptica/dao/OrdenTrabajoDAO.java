package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.OrdenTrabajo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenTrabajoDAO {
    private static final String INSERT_OT =
            "INSERT INTO ordenes_trabajo (numero_ot, id_cliente, id_recibo, id_tecnico, fecha_entrega, id_estado_ot, id_tipo_ot, observaciones, costo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_OT_BY_ID =
            "SELECT * FROM ordenes_trabajo WHERE id_orden = ?";
    private static final String SELECT_ALL_OTS =
            "SELECT * FROM ordenes_trabajo ORDER BY fecha_ingreso DESC";
    private static final String UPDATE_OT =
            "UPDATE ordenes_trabajo SET numero_ot=?, id_cliente=?, id_recibo=?, id_tecnico=?, fecha_entrega=?, id_estado_ot=?, id_tipo_ot=?, observaciones=?, costo=? WHERE id_orden=?";
    private static final String UPDATE_ESTADO =
            "UPDATE ordenes_trabajo SET id_estado_ot = ? WHERE id_orden = ?";

    private OrdenTrabajo mapearOT(ResultSet rs) throws SQLException {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setIdOrden(rs.getLong("id_orden"));
        ot.setNumeroOt(rs.getString("numero_ot"));
        ot.setIdCliente(rs.getLong("id_cliente"));
        ot.setIdRecibo(rs.getLong("id_recibo"));
        ot.setIdTecnico(rs.getLong("id_tecnico"));
        ot.setFechaIngreso(rs.getTimestamp("fecha_ingreso"));
        ot.setFechaEntrega(rs.getTimestamp("fecha_entrega"));
        ot.setIdEstadoOt(rs.getLong("id_estado_ot"));
        ot.setIdTipoOt(rs.getLong("id_tipo_ot"));
        ot.setObservaciones(rs.getString("observaciones"));
        ot.setCosto(rs.getBigDecimal("costo"));
        return ot;
    }

    // 1. Insertar (Asumimos que el número OT es generado en la lógica de negocio o BD)
    public Long insertar(OrdenTrabajo ot) throws SQLException {
        Long idGenerado = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(INSERT_OT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ot.getNumeroOt());
            ps.setLong(2, ot.getIdCliente());
            // Manejar nulos para id_recibo, id_tecnico, fecha_entrega
            if (ot.getIdRecibo() != null) ps.setLong(3, ot.getIdRecibo()); else ps.setNull(3, Types.BIGINT);
            if (ot.getIdTecnico() != null) ps.setLong(4, ot.getIdTecnico()); else ps.setNull(4, Types.BIGINT);
            if (ot.getFechaEntrega() != null) ps.setTimestamp(5, ot.getFechaEntrega()); else ps.setNull(5, Types.TIMESTAMP);

            ps.setLong(6, ot.getIdEstadoOt());
            ps.setLong(7, ot.getIdTipoOt());
            ps.setString(8, ot.getObservaciones());
            ps.setBigDecimal(9, ot.getCosto());

            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) { idGenerado = rs.getLong(1); }
            }
        } finally { ConexionBD.close(conn, ps, rs); }
        return idGenerado;
    }

    // 2. Listar
    public List<OrdenTrabajo> listarTodos() {
        List<OrdenTrabajo> ots = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_ALL_OTS);
            rs = ps.executeQuery();
            while (rs.next()) { ots.add(mapearOT(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return ots;
    }

    // 3. Obtener por ID
    public OrdenTrabajo obtenerPorId(Long id) {
        OrdenTrabajo ot = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_OT_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) { ot = mapearOT(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return ot;
    }

    // 4. Actualizar
    public boolean actualizar(OrdenTrabajo ot) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_OT);
            ps.setString(1, ot.getNumeroOt());
            ps.setLong(2, ot.getIdCliente());
            if (ot.getIdRecibo() != null) ps.setLong(3, ot.getIdRecibo()); else ps.setNull(3, Types.BIGINT);
            if (ot.getIdTecnico() != null) ps.setLong(4, ot.getIdTecnico()); else ps.setNull(4, Types.BIGINT);
            if (ot.getFechaEntrega() != null) ps.setTimestamp(5, ot.getFechaEntrega()); else ps.setNull(5, Types.TIMESTAMP);
            ps.setLong(6, ot.getIdEstadoOt());
            ps.setLong(7, ot.getIdTipoOt());
            ps.setString(8, ot.getObservaciones());
            ps.setBigDecimal(9, ot.getCosto());
            ps.setLong(10, ot.getIdOrden());
            return ps.executeUpdate() > 0;
        } finally { ConexionBD.close(conn, ps, null); }
    }

    // 5. Cambiar Estado
    public boolean cambiarEstado(Long idOrden, Long nuevoEstadoId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_ESTADO);
            ps.setLong(1, nuevoEstadoId);
            ps.setLong(2, idOrden);
            return ps.executeUpdate() > 0;
        } finally { ConexionBD.close(conn, ps, null); }
    }
}
