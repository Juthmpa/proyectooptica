package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.Prescripcion;
import java.sql.*;
import java.math.BigDecimal;
import java.util.Optional; // Para manejar IDs de forma segura

public class PrescripcionDAO {

    private static final String INSERT_PRESC =
            "INSERT INTO prescripciones (id_valoracion, sph_od, cyl_od, eje_od, sph_oi, cyl_oi, eje_oi, adicion, tipo_prescripcion_id, observaciones, id_optometrista) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_PRESC =
            "UPDATE prescripciones SET sph_od=?, cyl_od=?, eje_od=?, sph_oi=?, cyl_oi=?, eje_oi=?, adicion=?, tipo_prescripcion_id=?, observaciones=?, id_optometrista=? WHERE id_prescripcion=?";

    private static final String SELECT_PRESC_BY_ID =
            "SELECT * FROM prescripciones WHERE id_prescripcion = ?";

    private Prescripcion mapearPrescripcion(ResultSet rs) throws SQLException {
        Prescripcion p = new Prescripcion();
        p.setIdPrescripcion(rs.getLong("id_prescripcion"));
        p.setIdValoracion(rs.getLong("id_valoracion"));
        p.setSphOd(rs.getBigDecimal("sph_od"));
        p.setCylOd(rs.getBigDecimal("cyl_od"));
        p.setEjeOd(rs.getInt("eje_od"));
        p.setSphOi(rs.getBigDecimal("sph_oi"));
        p.setCylOi(rs.getBigDecimal("cyl_oi"));
        p.setEjeOi(rs.getInt("eje_oi"));
        p.setAdicion(rs.getBigDecimal("adicion"));
        p.setTipoPrescripcionId(rs.getLong("tipo_prescripcion_id"));
        p.setObservaciones(rs.getString("observaciones"));
        p.setIdOptometrista(rs.getLong("id_optometrista"));
        p.setFechaPrescripcion(rs.getTimestamp("fecha_creacion"));
        return p;
    }

    /**
     * Inserta una nueva Prescripción usando una conexión activa (transaccional).
     */
    public Long insertar(Connection conn, Long idValoracion, Prescripcion p) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Long idGenerado = null;
        try {
            ps = conn.prepareStatement(INSERT_PRESC, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, idValoracion);
            ps.setBigDecimal(2, Optional.ofNullable(p.getSphOd()).orElse(BigDecimal.ZERO));
            ps.setBigDecimal(3, Optional.ofNullable(p.getCylOd()).orElse(BigDecimal.ZERO));
            ps.setInt(4, Optional.ofNullable(p.getEjeOd()).orElse(0));
            ps.setBigDecimal(5, Optional.ofNullable(p.getSphOi()).orElse(BigDecimal.ZERO));
            ps.setBigDecimal(6, Optional.ofNullable(p.getCylOi()).orElse(BigDecimal.ZERO));
            ps.setInt(7, Optional.ofNullable(p.getEjeOi()).orElse(0));
            ps.setBigDecimal(8, Optional.ofNullable(p.getAdicion()).orElse(BigDecimal.ZERO));
            ps.setLong(9, p.getTipoPrescripcionId());
            ps.setString(10, p.getObservaciones());
            ps.setLong(11, p.getIdOptometrista());

            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) { idGenerado = rs.getLong(1); }
            }
        } finally {
            // IMPORTANTE: NO cerrar la conexión (conn), solo el PreparedStatement
            ConexionBD.close(null, ps, rs);
        }
        return idGenerado;
    }

    /**
     * Actualiza una Prescripción usando una conexión activa (transaccional).
     */
    public boolean actualizar(Connection conn, Prescripcion p) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(UPDATE_PRESC);
            ps.setBigDecimal(1, Optional.ofNullable(p.getSphOd()).orElse(BigDecimal.ZERO));
            ps.setBigDecimal(2, Optional.ofNullable(p.getCylOd()).orElse(BigDecimal.ZERO));
            ps.setInt(3, Optional.ofNullable(p.getEjeOd()).orElse(0));
            ps.setBigDecimal(4, Optional.ofNullable(p.getSphOi()).orElse(BigDecimal.ZERO));
            ps.setBigDecimal(5, Optional.ofNullable(p.getCylOi()).orElse(BigDecimal.ZERO));
            ps.setInt(6, Optional.ofNullable(p.getEjeOi()).orElse(0));
            ps.setBigDecimal(7, Optional.ofNullable(p.getAdicion()).orElse(BigDecimal.ZERO));
            ps.setLong(8, p.getTipoPrescripcionId());
            ps.setString(9, p.getObservaciones());
            ps.setLong(10, p.getIdOptometrista());
            ps.setLong(11, p.getIdPrescripcion());

            return ps.executeUpdate() > 0;
        } finally {
            ConexionBD.close(null, ps, null);
        }
    }

    /**
     * Obtiene una Prescripción por ID (No transaccional).
     */
    public Prescripcion obtenerPorId(Long id) {
        Prescripcion p = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_PRESC_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                p = mapearPrescripcion(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexionBD.close(conn, ps, rs);
        }
        return p;
    }
}
