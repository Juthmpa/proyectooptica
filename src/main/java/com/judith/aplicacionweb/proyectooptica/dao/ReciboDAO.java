package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.Recibo;
import com.judith.aplicacionweb.proyectooptica.model.DetalleRecibo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReciboDAO {
    private static final String INSERT_RECIBO =
            "INSERT INTO recibos (id_cliente, id_usuario, total, abono, saldo, id_forma_pago, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_RECIBO_BY_ID =
            "SELECT * FROM recibos WHERE id_recibo = ?";
    private static final String SELECT_ALL_RECIBOS =
            "SELECT * FROM recibos ORDER BY fecha_emision DESC";
    private static final String UPDATE_RECIBO =
            "UPDATE recibos SET id_cliente=?, id_usuario=?, total=?, abono=?, saldo=?, id_forma_pago=?, estado=? WHERE id_recibo=?";
    private static final String UPDATE_ESTADO =
            "UPDATE recibos SET estado = ? WHERE id_recibo = ?";

    private static final String INSERT_DETALLE =
            "INSERT INTO detalle_recibos (id_recibo, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";


    private Recibo mapearRecibo(ResultSet rs) throws SQLException {
        Recibo r = new Recibo();
        r.setIdRecibo(rs.getLong("id_recibo"));
        r.setIdCliente(rs.getLong("id_cliente"));
        r.setIdUsuario(rs.getLong("id_usuario"));
        r.setFechaEmision(rs.getTimestamp("fecha_emision"));
        r.setTotal(rs.getBigDecimal("total"));
        r.setAbono(rs.getBigDecimal("abono"));
        r.setSaldo(rs.getBigDecimal("saldo"));
        r.setIdFormaPago(rs.getLong("id_forma_pago"));
        r.setEstado(rs.getString("estado"));
        return r;
    }

    // 1. Insertar (Transaccional: Recibo + Detalle)
    public Long insertarReciboCompleto(Recibo recibo, List<DetalleRecibo> detalles) throws SQLException {
        Connection conn = null;
        PreparedStatement psRecibo = null;
        PreparedStatement psDetalle = null;
        ResultSet rs = null;
        Long idReciboGenerado = null;

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // Iniciar Transacción

            // 1. Insertar Encabezado
            psRecibo = conn.prepareStatement(INSERT_RECIBO, Statement.RETURN_GENERATED_KEYS);
            psRecibo.setLong(1, recibo.getIdCliente());
            psRecibo.setLong(2, recibo.getIdUsuario());
            psRecibo.setBigDecimal(3, recibo.getTotal());
            psRecibo.setBigDecimal(4, recibo.getAbono());
            psRecibo.setBigDecimal(5, recibo.getSaldo());
            psRecibo.setLong(6, recibo.getIdFormaPago());
            psRecibo.setString(7, recibo.getEstado());
            psRecibo.executeUpdate();

            rs = psRecibo.getGeneratedKeys();
            if (rs.next()) {
                idReciboGenerado = rs.getLong(1);
            } else {
                throw new SQLException("Fallo al obtener el ID del recibo.");
            }

            // 2. Insertar Detalles
            psDetalle = conn.prepareStatement(INSERT_DETALLE);
            for (DetalleRecibo det : detalles) {
                psDetalle.setLong(1, idReciboGenerado);
                psDetalle.setLong(2, det.getIdProducto());
                psDetalle.setInt(3, det.getCantidad());
                psDetalle.setBigDecimal(4, det.getPrecioUnitario());
                psDetalle.setBigDecimal(5, det.getSubtotal());
                psDetalle.addBatch(); // Agrega a un lote
            }
            psDetalle.executeBatch(); // Ejecuta todos los detalles

            conn.commit(); // Confirmar
            return idReciboGenerado;

        } catch (SQLException e) {
            if (conn != null) conn.rollback(); // Deshacer
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            ConexionBD.close(null, psRecibo, rs);
            ConexionBD.close(conn, psDetalle, null);
        }
    }

    // 2. Listar
    public List<Recibo> listarTodos() {
        List<Recibo> recibos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_ALL_RECIBOS);
            rs = ps.executeQuery();
            while (rs.next()) { recibos.add(mapearRecibo(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return recibos;
    }

    // 3. Obtener por ID
    public Recibo obtenerPorId(Long id) {
        Recibo r = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_RECIBO_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) { r = mapearRecibo(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return r;
    }

    // 4. Cambiar Estado
    public boolean cambiarEstado(Long idRecibo, String nuevoEstado) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_ESTADO);
            ps.setString(1, nuevoEstado);
            ps.setLong(2, idRecibo);
            return ps.executeUpdate() > 0;
        } finally { ConexionBD.close(conn, ps, null); }
    }
}