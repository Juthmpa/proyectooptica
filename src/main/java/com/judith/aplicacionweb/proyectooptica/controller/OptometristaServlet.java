package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.dao.OptometristaDAO;
import com.judith.aplicacionweb.proyectooptica.model.Optometrista;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "OptometristaServlet", urlPatterns = {"/app/gestion/optometristas", "/app/gestion/optometristas/*"})
public class OptometristaServlet extends HttpServlet {
    private OptometristaDAO optometristaDAO;

    @Override
    public void init() {
        optometristaDAO = new OptometristaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getPathInfo() != null && request.getPathInfo().equals("/save")) {
            insertOrUpdateOptometrista(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/gestion/optometristas");
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
                case "/delete":
                    deleteOptometrista(request, response);
                    break;
                case "":
                default:
                    listOptometristas(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("Error en el controlador de Optometristas", ex);
        }
    }

    private void listOptometristas(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<Optometrista> listaOptometristas = optometristaDAO.listarTodos();
        request.setAttribute("listaOptometristas", listaOptometristas);
        request.getRequestDispatcher("/WEB-INF/views/gestion/optometrista/optometristaList.jsp")
                .forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/gestion/optometrista/optometristaForm.jsp")
                .forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Optometrista existingOptometrista = optometristaDAO.obtenerPorId(id);
            request.setAttribute("optometrista", existingOptometrista);
            request.getRequestDispatcher("/WEB-INF/views/gestion/optometrista/optometristaForm.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de optometrista inválido.");
        }
    }

    private void insertOrUpdateOptometrista(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("idOptometrista");

        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String especialidad = request.getParameter("especialidad");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");

        Optometrista optometrista = new Optometrista();
        optometrista.setNombres(nombres);
        optometrista.setApellidos(apellidos);
        optometrista.setEspecialidad(especialidad);
        optometrista.setTelefono(telefono);
        optometrista.setCorreo(correo);

        String mensaje = "";

        try {
            if (idParam == null || idParam.isEmpty()) {
                optometristaDAO.insertar(optometrista);
                mensaje = "Optometrista '" + nombres + " " + apellidos + "' creado exitosamente.";
            } else {
                optometrista.setIdOptometrista(Long.parseLong(idParam));
                optometristaDAO.actualizar(optometrista);
                mensaje = "Optometrista '" + nombres + " " + apellidos + "' actualizado exitosamente.";
            }
        } catch (SQLException e) {
            mensaje = "Error SQL al procesar la solicitud: " + e.getMessage();
        } catch (Exception e) {
            mensaje = "Error inesperado: " + e.getMessage();
        }

        request.getSession().setAttribute("mensajeOptometrista", mensaje);
        response.sendRedirect(request.getContextPath() + "/app/gestion/optometristas");
    }

    private void deleteOptometrista(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            optometristaDAO.eliminar(id);

            request.getSession().setAttribute("mensajeOptometrista",
                    "Optometrista eliminado con éxito.");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("mensajeOptometrista",
                    "Error: ID de optometrista inválido.");

        } catch (SQLException e) {  // ← ESTE CATCH FALTABA
            request.getSession().setAttribute("mensajeOptometrista",
                    "Error SQL al eliminar: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/app/gestion/optometristas");
    }
}

