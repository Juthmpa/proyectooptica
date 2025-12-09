package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.model.Cita;
import com.judith.aplicacionweb.proyectooptica.dto.CitaRequestDTO;
import com.judith.aplicacionweb.proyectooptica.service.CitaService;
import com.judith.aplicacionweb.proyectooptica.service.CitaServiceImpl;
import com.judith.aplicacionweb.proyectooptica.exceptions.PermisoDenegadoException;

import jakarta.servlet.ServletException; // CAMBIO
import jakarta.servlet.annotation.WebServlet; // CAMBIO
import jakarta.servlet.http.HttpServlet; // CAMBIO
import jakarta.servlet.http.HttpServletRequest; // CAMBIO
import jakarta.servlet.http.HttpServletResponse; // CAMBIO

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/api/citas/*")
public class CitaServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final CitaService citaService = new CitaServiceImpl();
    private static final String PASSWORD_UNIVERSAL = "963";


    // Método auxiliar para obtener el ID del usuario (simulación)
    private Long obtenerIdUsuario(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        // Por defecto, asumimos 5L (ROL_CLIENTE) si no se especifica.
        return (userIdHeader != null) ? Long.parseLong(userIdHeader) : CitaService.ROL_CLIENTE;
    }

    /**
     * Maneja el método OPTIONS (Petición de pre-vuelo de CORS).
     * Esto es necesario para que el navegador sepa que el POST/PUT está permitido
     * desde un origen cruzado (cross-origin).
     */
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Estos encabezados son cruciales para que CORS funcione.
        // Asumimos que el CORSFilter también los establece, pero los reconfirmamos aquí.
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // Es importante incluir todos los encabezados personalizados que se envían (como X-User-Id).
        response.setHeader("Access-Control-Allow-Headers",
                "Content-Type, X-User-Id, X-Requested-With, accept, Authorization, origin");

        // Establecer el estado 200 OK (o 204 No Content) le dice al navegador que el pre-vuelo fue exitoso.
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // Metodo POST (Crear Cita)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idUsuario = obtenerIdUsuario(request);

        PrintWriter out = null; // Declaramos el escritor fuera del try

        try {
            // 1. Deserializar la petición
            CitaRequestDTO dto = gson.fromJson(request.getReader(), CitaRequestDTO.class);
            Cita cita = dto.toEntity();

            if (idUsuario.equals(CitaService.ROL_CLIENTE)) {
                // Si es cliente, aseguramos que solo pueda crear citas para sí mismo.
                cita.setIdCliente(idUsuario);
            }

            // 2. Ejecutar lógica de negocio
            Cita citaCreada = citaService.crearCita(cita, idUsuario);

            // 3. Configurar la respuesta exitosa (201 Created)
            response.setStatus(HttpServletResponse.SC_CREATED); // 201
            response.setContentType("application/json");

            // 4. Escribir el cuerpo de la respuesta
            out = response.getWriter();
            out.print(gson.toJson(citaCreada));
            out.flush();

        } catch (IllegalArgumentException e) {
            // Maneja errores de validación de datos (Ej: datos faltantes)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos: " + e.getMessage()); // 400
        } catch (PermisoDenegadoException e) {
            // Maneja errores de autorización
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage()); // 403
        } catch (RuntimeException e) {
            // Maneja errores de negocio (Ej: slot no disponible)
            response.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage()); // 409
        }
    }

    // Metodo GET (Buscar Citas)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");

            if (pathInfo != null && pathInfo.length() > 1) {
                // Leer por ID: /api/citas/123
                Long idCita = Long.parseLong(pathInfo.substring(1));
                Optional<Cita> cita = citaService.buscarPorId(idCita);

                if (cita.isPresent()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(gson.toJson(cita.get()));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cita no encontrada."); // 404
                }
            } else {
                // Listar todos / Filtrar
                Long idOptometrista = request.getParameter("idOptometrista") != null ? Long.parseLong(request.getParameter("idOptometrista")) : null;
                Map<String, Object> filtros = Map.of("idOptometrista", idOptometrista);

                List<Cita> citas = citaService.buscarCitas(filtros);

                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(citas));
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato de ID o filtro inválido."); // 400
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud."); // 500
        }
    }

    // Metodo PUT (Actualizar/Cancelar/Eliminar)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long idUsuario = obtenerIdUsuario(request);
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.split("/").length < 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de cita no especificado.");
            return;
        }

        String[] pathSegments = pathInfo.split("/");
        Long idCita = Long.parseLong(pathSegments[1]);
        String accion = pathSegments.length > 2 ? pathSegments[2] : null;

        try {
            if ("cancelar".equalsIgnoreCase(accion)) {
                Cita cita = citaService.cancelarCita(idCita, idUsuario);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().print(gson.toJson(cita));
            } else if ("eliminar".equalsIgnoreCase(accion)) {
                citaService.eliminarCitaPermanente(idCita, idUsuario);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
            } else {
                CitaRequestDTO dto = gson.fromJson(request.getReader(), CitaRequestDTO.class);
                dto.setIdCita(idCita);
                Cita citaActualizada = citaService.actualizarCita(dto.toEntity(), idUsuario);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().print(gson.toJson(citaActualizada));
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato de ID inválido."); // 400
        } catch (PermisoDenegadoException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage()); // 403
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage()); // 404
        }
    }
}