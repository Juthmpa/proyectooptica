package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.dao.CitaDAO;
import com.judith.aplicacionweb.proyectooptica.model.Cita;
import com.judith.aplicacionweb.proyectooptica.model.Cliente; // Necesario para mostrar FK
import com.judith.aplicacionweb.proyectooptica.model.Optometrista; // Necesario para mostrar FK
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "CitaServlet", urlPatterns = {"/app/gestion/citas", "/app/gestion/citas/*"})
public class CitaServlet extends HttpServlet {
    private CitaDAO citaDAO;
    // Asumimos que tienes DAO para Cliente, Optometrista y EstadoCita para los lookups
    // private ClienteDAO clienteDAO;
    // private OptometristaDAO optometristaDAO;
    // private LookupDAO lookupDAO;

    @Override
    public void init() {
        citaDAO = new CitaDAO();
        // Inicializar DAOs de lookup aquí...
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getPathInfo() != null && request.getPathInfo().equals("/save")) {
            insertOrUpdateCita(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/gestion/citas");
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
                    cambiarEstadoCita(request, response);
                    break;
                case "":
                default:
                    listCitas(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("Error en el controlador de Citas", ex);
        }
    }

    private void listCitas(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<Cita> listaCitas = citaDAO.listarTodos();
        request.setAttribute("listaCitas", listaCitas);
        // Opcional: cargar la lista de estados para el filtro
        request.getRequestDispatcher("/WEB-INF/views/gestion/cita/citaList.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cargar listas de Clientes, Optometristas, Estados para los Selects/Dropdowns
        request.getRequestDispatcher("/WEB-INF/views/gestion/cita/citaForm.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Cita existingCita = citaDAO.obtenerPorId(id);

            request.setAttribute("cita", existingCita);
            // Cargar listas de Clientes, Optometristas, Estados para los Selects/Dropdowns
            request.getRequestDispatcher("/WEB-INF/views/gestion/cita/citaForm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de cita inválido.");
        }
    }

    private void insertOrUpdateCita(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idParam = request.getParameter("idCita");

        // 1. Recolección de datos
        Long idCliente = Long.parseLong(request.getParameter("idCliente"));
        Long idOptometrista = Long.parseLong(request.getParameter("idOptometrista"));
        String fechaHoraStr = request.getParameter("fechaHora"); // Formato local datetime-local
        String motivo = request.getParameter("motivo");
        Long idEstado = Long.parseLong(request.getParameter("idEstado")); // Estado inicial o actual

        Cita cita = new Cita();
        cita.setIdCliente(idCliente);
        cita.setIdOptometrista(idOptometrista);
        cita.setMotivo(motivo);
        cita.setIdEstado(idEstado);

        // Convertir String de fecha y hora a java.sql.Timestamp
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(fechaHoraStr);
            cita.setFechaHora(Timestamp.valueOf(localDateTime));
        } catch (Exception e) {
            // Manejo de error de formato de fecha/hora
        }

        String mensaje = "";
        try {
            if (idParam == null || idParam.isEmpty()) {
                citaDAO.insertar(cita);
                mensaje = "Cita programada exitosamente.";
            } else {
                cita.setIdCita(Long.parseLong(idParam));
                citaDAO.actualizar(cita);
                mensaje = "Cita actualizada con éxito.";
            }
        } catch (Exception e) {
            mensaje = "Error al procesar la solicitud: " + e.getMessage();
        }

        request.getSession().setAttribute("mensajeCita", mensaje);
        response.sendRedirect(request.getContextPath() + "/app/gestion/citas");
    }

    /**
     * Cambia el estado de la cita (ej. a Atendida, Cancelada, Reprogramada).
     */
    private void cambiarEstadoCita(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Long nuevoEstadoId = Long.parseLong(request.getParameter("estadoId"));

            // ESTE MÉTODO LANZA SQLException → por eso debemos atraparlo
            citaDAO.cambiarEstado(id, nuevoEstadoId);

            request.getSession().setAttribute("mensajeCita",
                    "Estado de la cita cambiado con éxito.");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("mensajeCita",
                    "Error: ID de cita o estado inválido.");

        } catch (SQLException e) {   // ← ESTE CATCH FALTABA
            request.getSession().setAttribute("mensajeCita",
                    "Error SQL al cambiar estado: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/app/gestion/citas");
    }

}
