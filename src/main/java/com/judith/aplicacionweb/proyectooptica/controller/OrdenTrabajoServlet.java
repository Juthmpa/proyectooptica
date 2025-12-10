package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.dao.OrdenTrabajoDAO;
import com.judith.aplicacionweb.proyectooptica.model.OrdenTrabajo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "OrdenTrabajoServlet", urlPatterns = {"/app/gestion/ordenes", "/app/gestion/ordenes/*"})
public class OrdenTrabajoServlet extends HttpServlet {
    private OrdenTrabajoDAO ordenTrabajoDAO;

    @Override
    public void init() {
        ordenTrabajoDAO = new OrdenTrabajoDAO();
        // Inicializar DAOs de lookups (EstadoOT, TipoOT, Cliente, Usuario/Técnico, Recibo)
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getPathInfo() != null && request.getPathInfo().equals("/save")) {
            insertOrUpdateOrden(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/gestion/ordenes");
        }
    }

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
                case "/estado":
                    cambiarEstadoOrden(request, response);
                    break;
                case "":
                default:
                    listOrdenes(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("Error en el controlador de Órdenes de Trabajo", ex);
        }
    }

    private void listOrdenes(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<OrdenTrabajo> listaOrdenes = ordenTrabajoDAO.listarTodos();
        request.setAttribute("listaOrdenes", listaOrdenes);
        request.getRequestDispatcher("/WEB-INF/views/gestion/orden/ordenList.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cargar listas de Clientes, Tipos OT, Estados OT, Recibos (opcional), Técnicos
        request.getRequestDispatcher("/WEB-INF/views/gestion/orden/ordenForm.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            OrdenTrabajo existingOrden = ordenTrabajoDAO.obtenerPorId(id);

            request.setAttribute("orden", existingOrden);
            // Cargar listas de Clientes, Tipos OT, Estados OT, Recibos (opcional), Técnicos
            request.getRequestDispatcher("/WEB-INF/views/gestion/orden/ordenForm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de orden inválido.");
        }
    }

    private void insertOrUpdateOrden(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idParam = request.getParameter("idOrden");

        // 1. Recolección de datos
        // La fecha de ingreso y número OT pueden ser generados por el DAO
        Long idCliente = Long.parseLong(request.getParameter("idCliente"));
        Long idEstadoOt = Long.parseLong(request.getParameter("idEstadoOt"));
        Long idTipoOt = Long.parseLong(request.getParameter("idTipoOt"));
        String observaciones = request.getParameter("observaciones");
        BigDecimal costo = new BigDecimal(request.getParameter("costo"));
        // ... (obtener idRecibo, idTecnico, fechaEntrega)

        OrdenTrabajo orden = new OrdenTrabajo();
        orden.setIdCliente(idCliente);
        orden.setIdEstadoOt(idEstadoOt);
        orden.setIdTipoOt(idTipoOt);
        orden.setObservaciones(observaciones);
        orden.setCosto(costo);
        // ... setear otros campos

        String mensaje = "";

        try {
            if (idParam == null || idParam.isEmpty()) {
                // Asumimos que el DAO genera numero_ot y fecha_ingreso
                ordenTrabajoDAO.insertar(orden);
                mensaje = "Orden de Trabajo creada exitosamente.";
            } else {
                orden.setIdOrden(Long.parseLong(idParam));
                ordenTrabajoDAO.actualizar(orden);
                mensaje = "Orden de Trabajo actualizada exitosamente.";
            }
        } catch (Exception e) {
            mensaje = "Error al procesar la solicitud: " + e.getMessage();
        }

        request.getSession().setAttribute("mensajeOrden", mensaje);
        response.sendRedirect(request.getContextPath() + "/app/gestion/ordenes");
    }

    /**
     * Cambia el estado de la orden (PENDIENTE, EN_PROCESO, LISTO, ENTREGADO, CANCELADO).
     */
    private void cambiarEstadoOrden(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Long nuevoEstadoId = Long.parseLong(request.getParameter("estadoId"));

            // ordenTrabajoDAO.cambiarEstado(id, nuevoEstadoId);
            request.getSession().setAttribute("mensajeOrden", "Estado de la Orden de Trabajo actualizado.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("mensajeOrden", "Error: ID de orden o estado inválido.");
        }
        response.sendRedirect(request.getContextPath() + "/app/gestion/ordenes");
    }
}
