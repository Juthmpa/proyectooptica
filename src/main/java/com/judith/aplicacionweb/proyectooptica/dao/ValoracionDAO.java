package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.ValoracionVisual;
import com.judith.aplicacionweb.proyectooptica.model.Prescripcion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ValoracionDAO {

    private static final String INSERT_VALORACION =
            "INSERT INTO valoraciones_visuales (id_cliente, id_cita, id_optometrista, id_tipo_certificado, observaciones, prescripcion_id) " +
                    "VALUES (?, ?, ?, ?, ?, NULL)"; // prescripcion_id se actualiza después

    private static final String UPDATE_VALORACION_PRESC_ID =
            "UPDATE valoraciones_visuales SET prescripcion_id = ? WHERE id_valoracion = ?";

    private static final String UPDATE_VALORACION =
            "UPDATE valoraciones_visuales SET id_cliente=?, id_cita=?, id_optometrista=?, id_tipo_certificado=?, observaciones=? WHERE id_valoracion=?";

    private static final String SELECT_ALL_VALORACIONES =
            "SELECT * FROM valoraciones_visuales ORDER BY fecha_valoracion DESC";

    private static final String SELECT_VALORACION_BY_ID =
            "SELECT * FROM valoraciones_visuales WHERE id_valoracion = ?";

    private PrescripcionDAO prescripcionDAO = new PrescripcionDAO();

    private ValoracionVisual mapearValoracion(ResultSet rs) throws SQLException {
        ValoracionVisual vv = new ValoracionVisual();
        vv.setIdValoracion(rs.getLong("id_valoracion"));
        vv.setIdCliente(rs.getLong("id_cliente"));
        // El id_cita puede ser NULL
        vv.setIdCita(rs.getLong("id_cita"));
        if (rs.wasNull()) vv.setIdCita(null);

        vv.setIdOptometrista(rs.getLong("id_optometrista"));
        vv.setIdTipoCertificado(rs.getLong("id_tipo_certificado"));
        vv.setObservaciones(rs.getString("observaciones"));
        vv.setFechaValoracion(rs.getTimestamp("fecha_valoracion"));

        // El PrescripcionId es clave para cargar el objeto anidado
        Long prescripcionId = rs.getLong("prescripcion_id");
        if (!rs.wasNull()) {
            // Solo se guarda el ID aquí, la carga se hace en obtenerPorId
            Prescripcion p = new Prescripcion();
            p.setIdPrescripcion(prescripcionId);
            vv.setPrescripcion(p);
        }
        return vv;
    }

    /**
     * Inserta la Valoración y su Prescripción asociada en una única transacción.
     */
    public Long insertarValoracionConPrescripcion(ValoracionVisual vv) throws SQLException {
        Connection conn = null;
        PreparedStatement psV = null;
        ResultSet rs = null;
        Long idValoracionGenerado = null;

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // *** INICIO DE TRANSACCIÓN ***

            // 1. Insertar Valoración (sin PrescripcionId)
            psV = conn.prepareStatement(INSERT_VALORACION, Statement.RETURN_GENERATED_KEYS);
            psV.setLong(1, vv.getIdCliente());
            if (vv.getIdCita() != null) psV.setLong(2, vv.getIdCita()); else psV.setNull(2, Types.BIGINT);
            psV.setLong(3, vv.getIdOptometrista());
            psV.setLong(4, vv.getIdTipoCertificado());
            psV.setString(5, vv.getObservaciones());
            psV.executeUpdate();

            rs = psV.getGeneratedKeys();
            if (rs.next()) {
                idValoracionGenerado = rs.getLong(1);
            } else {
                throw new SQLException("Fallo al obtener ID de Valoración.");
            }

            // 2. Insertar Prescripción (si existe)
            if (vv.getPrescripcion() != null) {
                Prescripcion p = vv.getPrescripcion();
                Long idPrescripcionGenerado = prescripcionDAO.insertar(conn, idValoracionGenerado, p); // Usa la conexión activa

                // 3. Actualizar el campo prescripcion_id en la Valoración
                PreparedStatement psUpd = conn.prepareStatement(UPDATE_VALORACION_PRESC_ID);
                psUpd.setLong(1, idPrescripcionGenerado);
                psUpd.setLong(2, idValoracionGenerado);
                psUpd.executeUpdate();
                ConexionBD.close(null, psUpd, null);
            }

            conn.commit(); // *** FIN DE TRANSACCIÓN EXITOSO ***
            return idValoracionGenerado;

        } catch (SQLException e) {
            if (conn != null) conn.rollback(); // *** ROLLBACK EN CASO DE ERROR ***
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            ConexionBD.close(conn, psV, rs);
        }
    }

    /**
     * Actualiza la Valoración y su Prescripción asociada en una única transacción.
     */
    public boolean actualizarValoracionConPrescripcion(ValoracionVisual vv) throws SQLException {
        Connection conn = null;
        PreparedStatement psV = null;
        boolean exito = false;

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // *** INICIO DE TRANSACCIÓN ***

            // 1. Actualizar Valoración
            psV = conn.prepareStatement(UPDATE_VALORACION);
            psV.setLong(1, vv.getIdCliente());
            if (vv.getIdCita() != null) psV.setLong(2, vv.getIdCita()); else psV.setNull(2, Types.BIGINT);
            psV.setLong(3, vv.getIdOptometrista());
            psV.setLong(4, vv.getIdTipoCertificado());
            psV.setString(5, vv.getObservaciones());
            psV.setLong(6, vv.getIdValoracion());
            exito = psV.executeUpdate() > 0;

            // 2. Actualizar/Insertar Prescripción (si existe)
            if (vv.getPrescripcion() != null) {
                Prescripcion p = vv.getPrescripcion();
                p.setIdValoracion(vv.getIdValoracion()); // Asegurar FK

                if (p.getIdPrescripcion() != null && p.getIdPrescripcion() > 0) {
                    // Actualizar Prescripción existente
                    prescripcionDAO.actualizar(conn, p);
                } else {
                    // Insertar nueva Prescripción y actualizar FK en Valoración
                    Long idPrescripcionGenerado = prescripcionDAO.insertar(conn, vv.getIdValoracion(), p);

                    PreparedStatement psUpd = conn.prepareStatement(UPDATE_VALORACION_PRESC_ID);
                    psUpd.setLong(1, idPrescripcionGenerado);
                    psUpd.setLong(2, vv.getIdValoracion());
                    psUpd.executeUpdate();
                    ConexionBD.close(null, psUpd, null);
                }
            }

            conn.commit(); // *** FIN DE TRANSACCIÓN EXITOSO ***
            return exito;

        } catch (SQLException e) {
            if (conn != null) conn.rollback(); // *** ROLLBACK EN CASO DE ERROR ***
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            ConexionBD.close(conn, psV, null);
        }
    }

    // 3. Obtener por ID
    public ValoracionVisual obtenerPorId(Long id) {
        ValoracionVisual vv = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_VALORACION_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                vv = mapearValoracion(rs);

                // Cargar Prescripción
                if (vv.getPrescripcion() != null) {
                    vv.setPrescripcion(prescripcionDAO.obtenerPorId(vv.getPrescripcion().getIdPrescripcion()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexionBD.close(conn, ps, rs);
        }
        return vv;
    }

    // 4. Listar todas
    public List<ValoracionVisual> seleccionarTodas() {
        List<ValoracionVisual> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_ALL_VALORACIONES);
            rs = ps.executeQuery();
            while (rs.next()) {
                // Mapear solo la Valoración (se carga la Prescripción solo en 'obtenerPorId' o vista de detalle)
                lista.add(mapearValoracion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexionBD.close(conn, ps, rs);
        }
        return lista;
    }

    // 5. Eliminar (Si tu DDL usa CASCADE DELETE, es suficiente. Si no, debe ser transaccional)
    public boolean eliminar(Long id) throws SQLException {
        throw new UnsupportedOperationException("Implementar lógica de eliminación si es necesaria.");
    }
}
