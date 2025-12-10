package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String INSERT_CLIENTE =
            "INSERT INTO clientes (cedula, nombres, apellidos, fecha_nacimiento, telefono, direccion, correo, estado) VALUES (?, ?, ?, ?, ?, ?, ?, TRUE)";

    private static final String SELECT_CLIENTE_BY_ID =
            "SELECT * FROM clientes WHERE id_cliente = ?"; // Se obtiene estado para editar/mostrar

    private static final String SELECT_ALL_CLIENTES_ACTIVOS =
            "SELECT * FROM clientes WHERE estado = TRUE ORDER BY id_cliente DESC";

    private static final String UPDATE_CLIENTE =
            "UPDATE clientes SET cedula=?, nombres=?, apellidos=?, fecha_nacimiento=?, telefono=?, direccion=?, correo=? WHERE id_cliente=?";

    private static final String UPDATE_ESTADO =
            "UPDATE clientes SET estado = ? WHERE id_cliente = ?";

    // ---------------- Helper para mapear ResultSet a Cliente ----------------
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getLong("id_cliente"));
        cliente.setCedula(rs.getString("cedula"));
        cliente.setNombres(rs.getString("nombres"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        cliente.setEstado(rs.getBoolean("estado"));
        return cliente;
    }

    // ---------------- 1. Insertar ----------------
    public Long insertar(Cliente cliente) throws SQLException {
        Long idGenerado = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(INSERT_CLIENTE, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, cliente.getCedula());
            ps.setString(2, cliente.getNombres());
            ps.setString(3, cliente.getApellidos());
            ps.setDate(4, cliente.getFechaNacimiento());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getDireccion());
            ps.setString(7, cliente.getCorreo());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getLong(1);
                }
            }
        } finally {
            ConexionBD.close(conn, ps, rs);
        }
        return idGenerado;
    }

    // ---------------- 2. Listar todos (Activos) ----------------
    public List<Cliente> listarTodosActivos() {
        List<Cliente> clientes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_ALL_CLIENTES_ACTIVOS);
            rs = ps.executeQuery();
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexionBD.close(conn, ps, rs);
        }
        return clientes;
    }

    // ---------------- 3. Obtener cliente por ID ----------------
    public Cliente obtenerPorId(Long idCliente) {
        Cliente cliente = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_CLIENTE_BY_ID);
            ps.setLong(1, idCliente);
            rs = ps.executeQuery();

            if (rs.next()) {
                cliente = mapearCliente(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexionBD.close(conn, ps, rs);
        }
        return cliente;
    }

    // ---------------- 4. Actualizar ----------------
    public boolean actualizar(Cliente cliente) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_CLIENTE);

            ps.setString(1, cliente.getCedula());
            ps.setString(2, cliente.getNombres());
            ps.setString(3, cliente.getApellidos());
            ps.setDate(4, cliente.getFechaNacimiento());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getDireccion());
            ps.setString(7, cliente.getCorreo());
            ps.setLong(8, cliente.getIdCliente());

            return ps.executeUpdate() > 0;
        } finally {
            ConexionBD.close(conn, ps, null);
        }
    }

    // ---------------- 5. Cambiar Estado (Soft Delete) ----------------
    public boolean cambiarEstado(Long idCliente, boolean nuevoEstado) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_ESTADO);

            ps.setBoolean(1, nuevoEstado); // TRUE = activar, FALSE = desactivar
            ps.setLong(2, idCliente);

            return ps.executeUpdate() > 0;
        } finally {
            ConexionBD.close(conn, ps, null);
        }
    }
}