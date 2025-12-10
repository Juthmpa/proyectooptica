package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.dao.ProductoDAO;
import com.judith.aplicacionweb.proyectooptica.model.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/app/gestion/productos", "/app/gestion/productos/*"})
public class ProductoServlet extends HttpServlet {
    private ProductoDAO productoDAO;

    @Override
    public void init() {
        productoDAO = new ProductoDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("/save".equals(request.getPathInfo())) {
            insertOrUpdateProducto(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/gestion/productos");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getPathInfo() == null ? "" : request.getPathInfo();

        switch (action) {
            case "/new":
                showNewForm(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            case "/estado":
                cambiarEstadoProducto(request, response);
                break;
            default:
                listProductos(request, response);
                break;
        }
    }

    private void listProductos(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // ESTE NO LANZA SQLException
        List<Producto> listaProductos = productoDAO.listarTodosActivos();

        request.setAttribute("listaProductos", listaProductos);
        request.getRequestDispatcher("/WEB-INF/views/gestion/producto/productoList.jsp")
                .forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/gestion/producto/productoForm.jsp")
                .forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            // ESTE NO LANZA SQLException
            Producto existingProducto = productoDAO.obtenerPorId(id);

            request.setAttribute("producto", existingProducto);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/gestion/producto/productoForm.jsp")
                .forward(request, response);
    }

    private void insertOrUpdateProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("idProducto");

        Producto producto = new Producto();
        producto.setCodigo(request.getParameter("codigo"));
        producto.setNombre(request.getParameter("nombre"));
        producto.setIdTipoProducto(Long.parseLong(request.getParameter("idTipoProducto")));
        producto.setIdCategoria(Long.parseLong(request.getParameter("idCategoria")));
        producto.setDescripcion(request.getParameter("descripcion"));
        producto.setPrecio(new BigDecimal(request.getParameter("precio")));
        producto.setStock(Integer.parseInt(request.getParameter("stock")));

        producto.setIdMaterial(request.getParameter("idMaterial").isEmpty() ? null :
                Long.parseLong(request.getParameter("idMaterial")));

        producto.setIdTratamiento(request.getParameter("idTratamiento").isEmpty() ? null :
                Long.parseLong(request.getParameter("idTratamiento")));

        producto.setEstado(true);

        String mensaje;

        try {
            if (idParam == null || idParam.isEmpty()) {

                // ESTE LANZA SQLException
                productoDAO.insertar(producto);
                mensaje = "Producto creado correctamente.";

            } else {
                producto.setIdProducto(Long.parseLong(idParam));

                // ESTE LANZA SQLException
                productoDAO.actualizar(producto);
                mensaje = "Producto actualizado correctamente.";
            }

        } catch (SQLException e) {
            mensaje = "Error SQL: " + e.getMessage();
        }

        request.getSession().setAttribute("mensajeProducto", mensaje);
        response.sendRedirect(request.getContextPath() + "/app/gestion/productos");
    }

    private void cambiarEstadoProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            boolean nuevoEstado = "true".equalsIgnoreCase(request.getParameter("estado"));

            // ESTE LANZA SQLException
            productoDAO.cambiarEstado(id, nuevoEstado);

            request.getSession().setAttribute("mensajeProducto",
                    nuevoEstado ? "Producto activado." : "Producto desactivado.");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("mensajeProducto", "ID inválido.");
        } catch (SQLException e) {
            request.getSession().setAttribute("mensajeProducto", "Error SQL: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/app/gestion/productos");
    }
}


