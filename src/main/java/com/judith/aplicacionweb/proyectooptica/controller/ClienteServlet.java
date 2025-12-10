package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.dao.ClienteDAO;
import com.judith.aplicacionweb.proyectooptica.model.Cliente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

// Importante: Usamos la ruta /app/gestion/clientes/* para protegerla con el SecurityFilter
@WebServlet(name = "ClienteServlet", urlPatterns = {"/app/gestion/clientes", "/app/gestion/clientes/*"})
public class ClienteServlet extends HttpServlet {
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        clienteDAO = new ClienteDAO();
    }

    // Maneja CREATE y UPDATE (Vía Formulario)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verifica si la acción es guardar (insertar o actualizar)
        if (request.getPathInfo() != null && request.getPathInfo().equals("/save")) {
            insertOrUpdateCliente(request, response);
        } else {
            // Si no se encuentra la acción, redirigir al listado
            response.sendRedirect(request.getContextPath() + "/app/gestion/clientes");
        }
    }

    // Maneja READ (Listar, Mostrar Formulario) y la acción de Estado/Eliminar
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getPathInfo() == null ? "" : request.getPathInfo();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/delete": // Usa el mismo método que cambiarEstado para el soft delete
                case "/estado":
                    cambiarEstadoCliente(request, response);
                    break;
                case "": // Si no hay pathInfo (ruta base /app/gestion/clientes)
                default:
                    listClientes(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("Error de base de datos en el controlador de Clientes", ex);
        }
    }

    /**
     * Muestra la lista de todos los clientes activos.
     */
    private void listClientes(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<Cliente> listaClientes = clienteDAO.listarTodosActivos();
        request.setAttribute("listaClientes", listaClientes);

        request.getRequestDispatcher("/WEB-INF/views/gestion/cliente/clienteList.jsp")
                .forward(request, response);
    }

    /**
     * Muestra el formulario para crear un nuevo cliente.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // No necesita cargar datos, solo mostrar el formulario vacío
        request.getRequestDispatcher("/WEB-INF/views/gestion/cliente/clienteForm.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para editar un cliente existente.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Cliente existingCliente = clienteDAO.obtenerPorId(id);

            request.setAttribute("cliente", existingCliente);
            request.getRequestDispatcher("/WEB-INF/views/gestion/cliente/clienteForm.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de cliente inválido.");
        }
    }

    /**
     * Inserta un nuevo cliente o actualiza uno existente.
     */
    private void insertOrUpdateCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("idCliente");

        // 1. Recolección y mapeo de datos
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String cedula = request.getParameter("cedula");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");
        String direccion = request.getParameter("direccion");

        // Manejo de la fecha
        Date fechaNacimiento = null;
        try {
            String fechaStr = request.getParameter("fechaNacimiento");
            if (fechaStr != null && !fechaStr.isEmpty()) {
                fechaNacimiento = Date.valueOf(fechaStr);
            }
        } catch (IllegalArgumentException e) {
            // Manejar error de formato de fecha si es necesario
        }

        Cliente cliente = new Cliente();
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setCedula(cedula);
        cliente.setTelefono(telefono);
        cliente.setCorreo(correo);
        cliente.setDireccion(direccion);
        cliente.setFechaNacimiento(fechaNacimiento);

        boolean exito = false;
        String mensaje = "";

        try {
            if (idParam == null || idParam.isEmpty()) {
                // INSERTAR
                clienteDAO.insertar(cliente);
                mensaje = "Cliente '" + nombres + " " + apellidos + "' creado exitosamente.";
            } else {
                // ACTUALIZAR
                cliente.setIdCliente(Long.parseLong(idParam));
                exito = clienteDAO.actualizar(cliente);
                if (exito) {
                    mensaje = "Cliente '" + nombres + " " + apellidos + "' actualizado exitosamente.";
                } else {
                    mensaje = "Error al actualizar el cliente.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Error al procesar la solicitud: " + e.getMessage();
        }

        // Redirigir al listado con un mensaje de confirmación
        request.getSession().setAttribute("mensajeCliente", mensaje);
        response.sendRedirect(request.getContextPath() + "/app/gestion/clientes");
    }

    /**
     * Cambia el estado del cliente (eliminar lógico o reactivar).
     */
    private void cambiarEstadoCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            boolean nuevoEstado;
            String mensaje;

            if (request.getPathInfo().equals("/delete") ||
                    "false".equalsIgnoreCase(request.getParameter("estado"))) {
                nuevoEstado = false;
                mensaje = "Cliente desactivado correctamente.";
            } else {
                nuevoEstado = true;
                mensaje = "Cliente reactivado correctamente.";
            }

            try {
                clienteDAO.cambiarEstado(id, nuevoEstado);
            } catch (SQLException e) {
                e.printStackTrace();
                mensaje = "Error al actualizar el estado del cliente: " + e.getMessage();
            }

            request.getSession().setAttribute("mensajeCliente", mensaje);

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("mensajeCliente", "Error: ID inválido.");
        }

        response.sendRedirect(request.getContextPath() + "/app/gestion/clientes");
    }

}
