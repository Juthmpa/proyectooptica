package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.Optometrista;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OptometristaDAO {

    private static final String INSERT_OPTO =
            "INSERT INTO optometristas (nombres, apellidos, especialidad, telefono, correo) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_OPTO_BY_ID =
            "SELECT * FROM optometristas WHERE id_optometrista = ?";
    private static final String SELECT_ALL_OPTOS =
            "SELECT * FROM optometristas";
    private static final String UPDATE_OPTO =
            "UPDATE optometristas SET nombres=?, apellidos=?, especialidad=?, telefono=?, correo=? WHERE id_optometrista=?";
    private static final String DELETE_OPTO =
            "DELETE FROM optometristas WHERE id_optometrista = ?"; // Eliminación Física

    private Optometrista mapearOptometrista(ResultSet rs) throws SQLException {
        Optometrista opto = new Optometrista();
        opto.setIdOptometrista(rs.getLong("id_optometrista"));
        opto.setNombres(rs.getString("nombres"));
        opto.setApellidos(rs.getString("apellidos"));
        opto.setEspecialidad(rs.getString("especialidad"));
        opto.setTelefono(rs.getString("telefono"));
        opto.setCorreo(rs.getString("correo"));
        return opto;
    }

    // 1. Insertar
    public Long insertar(Optometrista opto) throws SQLException {
        Long idGenerado = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(INSERT_OPTO, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, opto.getNombres());
            ps.setString(2, opto.getApellidos());
            ps.setString(3, opto.getEspecialidad());
            ps.setString(4, opto.getTelefono());
            ps.setString(5, opto.getCorreo());

            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) { idGenerado = rs.getLong(1); }
            }
        } finally { ConexionBD.close(conn, ps, rs); }
        return idGenerado;
    }

    // 2. Listar
    public List<Optometrista> listarTodos() {
        List<Optometrista> optos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_ALL_OPTOS);
            rs = ps.executeQuery();
            while (rs.next()) { optos.add(mapearOptometrista(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return optos;
    }

    // 3. Obtener por ID
    public Optometrista obtenerPorId(Long id) {
        Optometrista opto = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_OPTO_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) { opto = mapearOptometrista(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return opto;
    }

    // 4. Actualizar
    public boolean actualizar(Optometrista opto) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_OPTO);
            ps.setString(1, opto.getNombres());
            ps.setString(2, opto.getApellidos());
            ps.setString(3, opto.getEspecialidad());
            ps.setString(4, opto.getTelefono());
            ps.setString(5, opto.getCorreo());
            ps.setLong(6, opto.getIdOptometrista());
            return ps.executeUpdate() > 0;
        } finally { ConexionBD.close(conn, ps, null); }
    }

    // 5. Eliminar (Físico)
    public boolean eliminar(Long id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(DELETE_OPTO);
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } finally { ConexionBD.close(conn, ps, null); }
    }
}