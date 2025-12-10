package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.dao.ValoracionDAO;
import com.judith.aplicacionweb.proyectooptica.model.ValoracionVisual;
import com.judith.aplicacionweb.proyectooptica.model.Prescripcion;
import com.judith.aplicacionweb.proyectooptica.model.Usuario; // Para obtener el ID del Optometrista
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ValoracionServlet", urlPatterns = "/app/optometria/valoraciones/*")
public class ValoracionServlet extends HttpServlet {

    private ValoracionDAO valoracionDAO;
    // Otros DAOs (ClienteDAO, OptometristaDAO, LookupDAO) para cargar listas en el formulario

    public void init() {
        valoracionDAO = new ValoracionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo() == null ? "" : request.getPathInfo();

        if (action.equals("/save")) {
            saveOrUpdateValoracion(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/optometria/valoraciones");
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
                    // deleteValoracion(request, response);
                    // No implementado completamente en DAO, usar con cautela
                    break;
                case "":
                default:
                    listValoraciones(request, response);
                    break;
            }
        } catch (SQLException ex) {
            // Manejo de errores de base de datos
            request.getSession().setAttribute("mensajeValoracion", "Error de base de datos: " + ex.getMessage());
            response.sendRedirect(request.getContextPath() + "/app/optometria/valoraciones");
        }
    }

    private void listValoraciones(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        List<ValoracionVisual> listaValoraciones = valoracionDAO.seleccionarTodas();
        request.setAttribute("listaValoraciones", listaValoraciones);
        request.getRequestDispatcher("/WEB-INF/views/optometria/valoracion/valoracionesList.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Cargar listas de Clientes, Tipos de Certificado, etc.
        request.getRequestDispatcher("/WEB-INF/views/optometria/valoracion/valoracionForm.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String mensaje = null;
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            ValoracionVisual existingValoracion = valoracionDAO.obtenerPorId(id);

            if (existingValoracion == null) {
                mensaje = "Valoración no encontrada.";
                throw new Exception(mensaje);
            }

            request.setAttribute("valoracion", existingValoracion);
            // Cargar lookups para el formulario
            request.getRequestDispatcher("/WEB-INF/views/optometria/valoracion/valoracionForm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            mensaje = "ID de valoración inválido.";
        } catch (Exception e) {
            mensaje = mensaje != null ? mensaje : "Error al cargar la valoración: " + e.getMessage();
        }

        if (mensaje != null) {
            request.getSession().setAttribute("mensajeValoracion", mensaje);
            response.sendRedirect(request.getContextPath() + "/app/optometria/valoraciones");
        }
    }

    private void saveOrUpdateValoracion(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String mensaje = null;
        try {
            HttpSession session = request.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

            if (usuario == null) throw new SecurityException("Usuario no autenticado.");

            // **TODO CRÍTICO: Obtener el id_optometrista real a partir del id_usuario**
            Long idOptometrista = 1L; // MOCK: Asume ID 1

            // --- 1. Mapeo de Valoración ---
            String idValoracionParam = request.getParameter("idValoracion");
            ValoracionVisual vv = new ValoracionVisual();

            if (idValoracionParam != null && !idValoracionParam.isEmpty()) {
                vv.setIdValoracion(Long.parseLong(idValoracionParam));
            }

            vv.setIdOptometrista(idOptometrista);
            vv.setIdCliente(Long.parseLong(request.getParameter("idCliente")));
            vv.setIdTipoCertificado(Long.parseLong(request.getParameter("idTipoCertificado")));
            vv.setObservaciones(request.getParameter("observacionesValoracion"));

            String idCitaParam = request.getParameter("idCita");
            if (idCitaParam != null && !idCitaParam.isEmpty()) {
                vv.setIdCita(Long.parseLong(idCitaParam));
            }

            // --- 2. Mapeo de Prescripción (Si fue llenada) ---
            if ("true".equalsIgnoreCase(request.getParameter("incluirPrescripcion"))) {
                Prescripcion p = new Prescripcion();

                String idPrescripcionParam = request.getParameter("idPrescripcion");
                if (idPrescripcionParam != null && !idPrescripcionParam.isEmpty()) {
                    p.setIdPrescripcion(Long.parseLong(idPrescripcionParam));
                }

                p.setIdOptometrista(idOptometrista);

                p.setSphOd(parseBigDecimal(request.getParameter("sphOd")));
                p.setCylOd(parseBigDecimal(request.getParameter("cylOd")));
                p.setEjeOd(parseInteger(request.getParameter("ejeOd")));
                p.setSphOi(parseBigDecimal(request.getParameter("sphOi")));
                p.setCylOi(parseBigDecimal(request.getParameter("cylOi")));
                p.setEjeOi(parseInteger(request.getParameter("ejeOi")));
                p.setAdicion(parseBigDecimal(request.getParameter("adicion")));

                p.setTipoPrescripcionId(Long.parseLong(request.getParameter("tipoPrescripcionId")));
                p.setObservaciones(request.getParameter("observacionesPrescripcion"));

                vv.setPrescripcion(p);
            }

            // 3. Ejecución del DAO
            if (vv.getIdValoracion() == null) {
                valoracionDAO.insertarValoracionConPrescripcion(vv);
                mensaje = "Valoración y Prescripción registradas exitosamente.";
            } else {
                valoracionDAO.actualizarValoracionConPrescripcion(vv);
                mensaje = "Valoración y Prescripción actualizadas exitosamente.";
            }

        } catch (NumberFormatException e) {
            mensaje = "Error: Asegúrate de que todos los campos numéricos (ID, esferas, cilindros, ejes, adición) sean válidos.";
        } catch (SQLException e) {
            mensaje = "Error de base de datos al guardar: " + e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            mensaje = "Error al procesar la solicitud: " + e.getMessage();
        }

        request.getSession().setAttribute("mensajeValoracion", mensaje);
        response.sendRedirect(request.getContextPath() + "/app/optometria/valoraciones");
    }

    // --- Helpers para manejar campos numéricos vacíos ---
    private BigDecimal parseBigDecimal(String param) {
        if (param == null || param.trim().isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(param.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Valor decimal inválido: " + param);
        }
    }

    private Integer parseInteger(String param) {
        if (param == null || param.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(param.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Valor entero inválido: " + param);
        }
    }
}