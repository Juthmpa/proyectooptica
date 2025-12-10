package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.Rol;
import com.judith.aplicacionweb.proyectooptica.dao.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {

    // --- Consultas SQL ---
    private static final String SELECT_ALL_ROLES =
            "SELECT id_rol, nombre, descripcion FROM roles ORDER BY id_rol";
    private static final String SELECT_ROL_BY_ID =
            "SELECT id_rol, nombre, descripcion FROM roles WHERE id_rol = ?";
    private static final String INSERT_ROL =
            "INSERT INTO roles (nombre, descripcion) VALUES (?, ?)";
    private static final String UPDATE_ROL =
            "UPDATE roles SET nombre = ?, descripcion = ? WHERE id_rol = ?";
    private static final String DELETE_ROL =
            "DELETE FROM roles WHERE id_rol = ?"; // Usado solo para mantenimiento


    // --- 1. Listar Todos los Roles (READ - CRUCIAL para UsuarioController) ---

    public List<Rol> seleccionarTodos() throws SQLException {
        List<Rol> roles = new ArrayList<>();
        // El try-with-resources asegura que la conexión se cierre automáticamente
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_ROLES);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol();
                rol.setIdRol(rs.getLong("id_rol"));
                rol.setNombre(rs.getString("nombre"));
                // Aquí podrías setear la descripción si es necesario

                roles.add(rol);
            }
        }
        return roles;
    }

    // --- 2. Seleccionar Rol por ID (READ) ---

    public Rol seleccionarPorId(Long idRol) throws SQLException {
        Rol rol = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ROL_BY_ID)) {

            ps.setLong(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rol = new Rol();
                    rol.setIdRol(rs.getLong("id_rol"));
                    rol.setNombre(rs.getString("nombre"));
                }
            }
        }
        return rol;
    }

    // --- 3. Insertar Nuevo Rol (CREATE) ---

    public void insertar(Rol rol) throws SQLException {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ROL)) {

            ps.setString(1, rol.getNombre());
            // Asume que el modelo Rol tiene un método getDescripcion()
            // ps.setString(2, rol.getDescripcion());
            ps.setString(2, "Descripción por defecto"); // Si el modelo Rol aún no tiene descripción

            ps.executeUpdate();
        }
    }

    // --- 4. Actualizar Rol (UPDATE) ---

    public boolean actualizar(Rol rol) throws SQLException {
        boolean rowUpdated;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_ROL)) {

            ps.setString(1, rol.getNombre());
            // ps.setString(2, rol.getDescripcion());
            ps.setString(2, "Descripción actualizada");
            ps.setLong(3, rol.getIdRol());

            rowUpdated = ps.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // --- 5. Eliminar Rol (DELETE) ---

    public boolean eliminar(Long idRol) throws SQLException {
        boolean rowDeleted;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ROL)) {

            ps.setLong(1, idRol);
            rowDeleted = ps.executeUpdate() > 0;
        }
        return rowDeleted;
    }
}
