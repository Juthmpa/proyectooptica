package com.judith.aplicacionweb.proyectooptica.dao;

import com.judith.aplicacionweb.proyectooptica.model.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private static final String INSERT_PRODUCTO =
            "INSERT INTO productos (codigo, nombre, id_tipo_producto, id_categoria, descripcion, precio, stock, id_material, id_tratamiento, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, TRUE)";
    private static final String SELECT_PRODUCTO_BY_ID =
            "SELECT * FROM productos WHERE id_producto = ?";
    private static final String SELECT_ALL_PRODUCTOS_ACTIVOS =
            "SELECT * FROM productos WHERE estado = TRUE";
    private static final String UPDATE_PRODUCTO =
            "UPDATE productos SET codigo=?, nombre=?, id_tipo_producto=?, id_categoria=?, descripcion=?, precio=?, stock=?, id_material=?, id_tratamiento=? WHERE id_producto=?";
    private static final String UPDATE_ESTADO =
            "UPDATE productos SET estado = ? WHERE id_producto = ?";

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getLong("id_producto"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setIdTipoProducto(rs.getLong("id_tipo_producto"));
        p.setIdCategoria(rs.getLong("id_categoria"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setIdMaterial(rs.getLong("id_material"));
        p.setIdTratamiento(rs.getLong("id_tratamiento"));
        p.setEstado(rs.getBoolean("estado"));
        // Manejar nulos para los FKs de Material y Tratamiento
        if (rs.wasNull()) p.setIdMaterial(null);
        p.setIdTratamiento(rs.getLong("id_tratamiento"));
        if (rs.wasNull()) p.setIdTratamiento(null);

        return p;
    }

    // 1. Insertar
    public Long insertar(Producto p) throws SQLException {
        Long idGenerado = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(INSERT_PRODUCTO, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setLong(3, p.getIdTipoProducto());
            ps.setLong(4, p.getIdCategoria());
            ps.setString(5, p.getDescripcion());
            ps.setBigDecimal(6, p.getPrecio());
            ps.setInt(7, p.getStock());

            // Manejar nulos para id_material y id_tratamiento
            if (p.getIdMaterial() != null) ps.setLong(8, p.getIdMaterial()); else ps.setNull(8, Types.BIGINT);
            if (p.getIdTratamiento() != null) ps.setLong(9, p.getIdTratamiento()); else ps.setNull(9, Types.BIGINT);

            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) { idGenerado = rs.getLong(1); }
            }
        } finally { ConexionBD.close(conn, ps, rs); }
        return idGenerado;
    }

    // 2. Listar
    public List<Producto> listarTodosActivos() {
        List<Producto> productos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_ALL_PRODUCTOS_ACTIVOS);
            rs = ps.executeQuery();
            while (rs.next()) { productos.add(mapearProducto(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return productos;
    }

    // 3. Obtener por ID
    public Producto obtenerPorId(Long id) {
        Producto p = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(SELECT_PRODUCTO_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) { p = mapearProducto(rs); }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { ConexionBD.close(conn, ps, rs); }
        return p;
    }

    // 4. Actualizar
    public boolean actualizar(Producto p) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_PRODUCTO);
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setLong(3, p.getIdTipoProducto());
            ps.setLong(4, p.getIdCategoria());
            ps.setString(5, p.getDescripcion());
            ps.setBigDecimal(6, p.getPrecio());
            ps.setInt(7, p.getStock());

            if (p.getIdMaterial() != null) ps.setLong(8, p.getIdMaterial()); else ps.setNull(8, Types.BIGINT);
            if (p.getIdTratamiento() != null) ps.setLong(9, p.getIdTratamiento()); else ps.setNull(9, Types.BIGINT);
            ps.setLong(10, p.getIdProducto());

            return ps.executeUpdate() > 0;
        } finally { ConexionBD.close(conn, ps, null); }
    }

    // 5. Cambiar Estado
    public boolean cambiarEstado(Long idProducto, boolean nuevoEstado) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConexionBD.getConnection();
            ps = conn.prepareStatement(UPDATE_ESTADO);
            ps.setBoolean(1, nuevoEstado);
            ps.setLong(2, idProducto);
            return ps.executeUpdate() > 0;
        } finally { ConexionBD.close(conn, ps, null); }
    }
}
