package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.Rol;
import com.judith.aplicacionweb.proyectooptica.model.Usuario;
import com.judith.aplicacionweb.proyectooptica.util.PasswordHashUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsuarioDAO {

    // Autenticación y Carga de Rol
    private static final String AUTH_QUERY =
            "SELECT u.id_usuario, u.username, u.password_hash, u.nombre_completo, u.estado, r.id_rol, r.nombre AS nombre_rol " +
                    "FROM usuarios u JOIN roles r ON u.id_rol = r.id_rol WHERE u.username = ?";

    // Carga de Permisos por Rol (Se mantiene igual, no cambia)
    private static final String PERMISSIONS_QUERY =
            "SELECT p.codigo FROM permisos p " +
                    "JOIN rol_permiso rp ON p.id_permiso = rp.id_permiso " +
                    "WHERE rp.id_rol = ?";

    // CRUD
    private static final String INSERT_USER =
            "INSERT INTO usuarios (id_rol, username, password_hash, nombre_completo, correo, telefono, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_USERS =
            "SELECT u.id_usuario, u.username, u.nombre_completo, u.correo, u.telefono, u.estado, r.id_rol, r.nombre AS nombre_rol " +
                    "FROM usuarios u JOIN roles r ON u.id_rol = r.id_rol ORDER BY u.id_usuario";

    private static final String SELECT_USER_BY_ID =
            "SELECT u.id_usuario, u.username, u.password_hash, u.nombre_completo, u.correo, u.telefono, u.estado, r.id_rol, r.nombre AS nombre_rol " +
                    "FROM usuarios u JOIN roles r ON u.id_rol = r.id_rol WHERE u.id_usuario = ?";

    private static final String UPDATE_USER =
            "UPDATE usuarios SET id_rol = ?, username = ?, password_hash = ?, nombre_completo = ?, correo = ?, telefono = ?, estado = ? WHERE id_usuario = ?";

    // Cambio de Estado (Soft Delete / Reactivación)
    private static final String UPDATE_STATUS =
            "UPDATE usuarios SET estado = ? WHERE id_usuario = ?";

    // -----------------------------------------------------------------------------------------------------
    // --- 1. Autenticación (Ahora usa password_hash y compara con hash real) ---
    // -----------------------------------------------------------------------------------------------------

    public Usuario autenticar(String username, String rawPassword) throws SQLException {
        Usuario usuario = null;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(AUTH_QUERY)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    boolean activo = rs.getBoolean("estado");
                    if (!activo) {
                        return null; // Usuario inactivo → no permitir
                    }

                    String storedHash = rs.getString("password_hash");

                    if (PasswordHashUtil.checkPassword(rawPassword, storedHash)) {

                        usuario = new Usuario();
                        usuario.setId(rs.getLong("id_usuario"));
                        usuario.setUsername(rs.getString("username"));
                        usuario.setNombreCompleto(rs.getString("nombre_completo"));
                        usuario.setActivo(true); // porque ya se verificó

                        Rol rol = new Rol();
                        rol.setIdRol(rs.getLong("id_rol"));
                        rol.setNombre(rs.getString("nombre_rol"));
                        usuario.setRol(rol);

                        usuario.setCodigosPermiso(
                                cargarCodigosPermisoPorRol(conn, rol.getIdRol())
                        );
                    }
                }
            }
        }
        return usuario;
    }


    private Set<String> cargarCodigosPermisoPorRol(Connection conn, Long idRol) throws SQLException {
        Set<String> codigosPermisos = new HashSet<>();
        try (PreparedStatement ps = conn.prepareStatement(PERMISSIONS_QUERY)) {
            ps.setLong(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    codigosPermisos.add(rs.getString("codigo"));
                }
            }
        }
        return codigosPermisos;
    }


    // -----------------------------------------------------------------------------------------------------
    // --- 2. Listar Todos los Usuarios (READ) ---
    // -----------------------------------------------------------------------------------------------------

    public List<Usuario> seleccionarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(SELECT_ALL_USERS)) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setActivo(rs.getBoolean("estado"));

                Rol rol = new Rol();
                rol.setIdRol(rs.getLong("id_rol"));
                rol.setNombre(rs.getString("nombre_rol"));
                usuario.setRol(rol);

                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    // -----------------------------------------------------------------------------------------------------
    // --- 3. Insertar Nuevo Usuario (CREATE) ---
    // -----------------------------------------------------------------------------------------------------

    public void insertar(Usuario usuario) throws SQLException {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER)) {

            ps.setLong(1, usuario.getRol().getIdRol());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getPasswordHash()); // Ya debe venir hasheado del Controller
            ps.setString(4, usuario.getNombreCompleto());
            ps.setString(5, usuario.getCorreo());
            ps.setString(6, usuario.getTelefono());
            ps.setBoolean(7, usuario.getActivo()); // Si no viene, el DAO/BD usa TRUE por defecto

            ps.executeUpdate();
        }
    }

    // -----------------------------------------------------------------------------------------------------
    // --- 4. Seleccionar Usuario por ID (READ) ---
    // -----------------------------------------------------------------------------------------------------

    public Usuario seleccionarPorId(Long idUsuario) throws SQLException {
        Usuario usuario = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_ID)) {

            ps.setLong(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getLong("id_usuario"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPasswordHash(rs.getString("password_hash")); // Útil para rellenar el formulario de edición (pero no se muestra)
                    usuario.setNombreCompleto(rs.getString("nombre_completo"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setActivo(rs.getBoolean("estado"));

                    Rol rol = new Rol();
                    rol.setIdRol(rs.getLong("id_rol"));
                    rol.setNombre(rs.getString("nombre_rol"));
                    usuario.setRol(rol);
                }
            }
        }
        return usuario;
    }

    // -----------------------------------------------------------------------------------------------------
    // --- 5. Actualizar Usuario (UPDATE) ---
    // -----------------------------------------------------------------------------------------------------

    public boolean actualizar(Usuario usuario) throws SQLException {
        boolean rowUpdated;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER)) {

            ps.setLong(1, usuario.getRol().getIdRol());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getPasswordHash());
            ps.setString(4, usuario.getNombreCompleto());
            ps.setString(5, usuario.getCorreo());
            ps.setString(6, usuario.getTelefono());
            ps.setBoolean(7, usuario.getActivo());
            ps.setLong(8, usuario.getId());

            rowUpdated = ps.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // -----------------------------------------------------------------------------------------------------
    // --- 6. CAMBIO DE ESTADO (Soft Delete / Reactivación) ---
    // -----------------------------------------------------------------------------------------------------

    public boolean cambiarEstado(Long idUsuario, boolean nuevoEstado) throws SQLException {
        boolean rowUpdated;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS)) {

            ps.setBoolean(1, nuevoEstado);
            ps.setLong(2, idUsuario);

            rowUpdated = ps.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}