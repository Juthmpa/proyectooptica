package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.dao.PrescripcionDAO;
import com.judith.aplicacionweb.proyectooptica.model.Prescripcion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
// Si se implementa listar, se necesitaría un método en el DAO

@WebServlet(name = "PrescripcionServlet", urlPatterns = {"/app/optometria/prescripciones", "/app/optometria/prescripciones/*"})
public class PrescripcionServlet extends HttpServlet {
    private PrescripcionDAO prescripcionDAO;

    @Override
    public void init() {
        prescripcionDAO = new PrescripcionDAO();
    }

    // No se implementa doPost, la lógica de guardar/actualizar está en ValoracionServlet

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getPathInfo() == null ? "" : request.getPathInfo();
        String mensaje = null;

        try {
            switch (action) {
                case "/view":
                    showPrescripcionDetails(request, response);
                    break;
                case "":
                default:
                    // Si no hay método listar, simplemente se redirige
                    response.sendRedirect(request.getContextPath() + "/app/optometria/valoraciones");
                    break;
            }
        } catch (Exception ex) {
            mensaje = "Error en el controlador de Prescripciones: " + ex.getMessage();
            request.getSession().setAttribute("mensajePrescripcion", mensaje);
            response.sendRedirect(request.getContextPath() + "/app/optometria/valoraciones");
        }
    }

    private void showPrescripcionDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mensaje = null;
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Prescripcion prescripcion = prescripcionDAO.obtenerPorId(id);

            if (prescripcion == null) {
                mensaje = "Prescripción no encontrada.";
                throw new Exception(mensaje);
            }

            request.setAttribute("prescripcion", prescripcion);
            // El front-end debe enlazar a la Valoración asociada si existe (usando prescripcion.getIdValoracion())
            request.getRequestDispatcher("/WEB-INF/views/optometria/prescripcion/prescripcionDetails.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            mensaje = "ID de prescripción inválido.";
        } catch (Exception e) {
            mensaje = mensaje != null ? mensaje : "Error al cargar la prescripción: " + e.getMessage();
        }

        if (mensaje != null) {
            request.getSession().setAttribute("mensajePrescripcion", mensaje);
            response.sendRedirect(request.getContextPath() + "/app/optometria/valoraciones");
        }
    }
}