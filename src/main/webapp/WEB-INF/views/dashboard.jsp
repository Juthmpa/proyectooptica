<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 12/11/2025
  Time: 20:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="com.judith.aplicacionweb.proyectooptica.model.Usuario" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%
    // VALIDACIÓN DE SESIÓN
    Usuario usuarioLog = (Usuario) session.getAttribute("usuarioSesion");
    if (usuarioLog == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Set<String> permisos = (Set<String>) session.getAttribute("permisosUsuario");
    if (permisos == null) {
        permisos = Set.of(); // evita NPE
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Óptica Joesva</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        :root {
            --color-primary: #44B4EB; /* Azul */
            --color-dark-green: #188C7C; /* Verde Principal */
            --color-accent: #30A09C; /* Verde Secundario */
            --color-success: #14A47C; /* Verde de Éxito */
        }
        .header-bg { background-color: var(--color-dark-green); color: white; }
        .btn-outline-light { border-color: white !important; }
        .btn-outline-light:hover { background-color: rgba(255, 255, 255, 0.1); }
        .text-accent { color: var(--color-accent) !important; }
        .bg-primary-light { background-color: var(--color-primary); }
        .badge-optometra { background-color: var(--color-success); }
        .list-group-item-action:hover { background-color: #f0f8ff; }
        .badge-recepcion { background-color: var(--color-accent); }
        .badge-ventas { background-color: var(--color-dark-green); }
    </style>
</head>

<body class="bg-light">

<div class="header-bg p-3 shadow-sm">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center">
            <h1 class="h3 mb-0">
                Bienvenido al Sistema,
                <span class="fw-bold"><%= session.getAttribute("username") %></span>!
            </h1>

            <div>
                <span class="badge bg-info text-dark me-3">
                    Rol: <%= usuarioLog.getRol() != null ? usuarioLog.getRol().getNombre() : "N/A" %>
                </span>
                <a href="<%= request.getContextPath() %>/logout" class="btn btn-sm btn-outline-light">Cerrar Sesión</a>
            </div>
        </div>
    </div>
</div>

<div class="container mt-5">
    <div class="row">

        <div class="col-md-7">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white">
                    <h2 class="h4 mb-0 text-accent"><i class="bi bi-grid-fill me-2"></i> Módulos de Gestión</h2>
                </div>

                <div class="list-group list-group-flush">

                    <%-- === GRUPO 1: OPTOMETRÍA (ValoracionServlet y PrescripcionServlet) === --%>
                    <% if (permisos.contains("CONSULTAR_VALORACIONES")) { %>
                    <a href="<%= request.getContextPath() %>/app/optometria/valoraciones"
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                        <i class="bi bi-eye-fill me-2"></i> Valoraciones Visuales y Prescripción
                        <span class="badge badge-optometra">Optómetra</span>
                    </a>
                    <% } %>

                    <a href="<%= request.getContextPath() %>/app/optometria/prescripciones"
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                        <i class="bi bi-journal-medical me-2"></i> Prescripciones (Ver Detalle)
                        <span class="badge badge-optometra">Optómetra</span>
                    </a>

                    <%-- === GRUPO 2: GESTIÓN DE PERSONAS (ClienteServlet y OptometristaServlet) === --%>
                    <a href="<%= request.getContextPath() %>/app/gestion/clientes"
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                        <i class="bi bi-person-badge-fill me-2"></i> **Gestión de Clientes/Pacientes**
                        <span class="badge badge-recepcion">Recepción</span>
                    </a>

                    <a href="<%= request.getContextPath() %>/app/gestion/optometristas"
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                        <i class="bi bi-person-rolodex me-2"></i> Gestión de Optometristas
                        <span class="badge bg-primary-light rounded-pill">Admin</span>
                    </a>

                    <%-- === GRUPO 3: AGENDA (CitaServlet) === --%>
                    <a href="<%= request.getContextPath() %>/app/agenda/citas"
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                        <i class="bi bi-calendar-check-fill me-2"></i> Agenda de Citas
                        <span class="badge badge-recepcion">Recepción</span>
                    </a>

                    <%-- === GRUPO 4: VENTAS y TALLER (ReciboServlet y OrdenTrabajoServlet) === --%>
                    <a href="<%= request.getContextPath() %>/app/ventas/recibos"
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                        <i class="bi bi-cash-stack me-2"></i> Ventas y Recibos
                        <span class="badge badge-ventas">Vendedor</span>
                    </a>

                    <a href="<%= request.getContextPath() %>/app/taller/ordenes"
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                        <i class="bi bi-tools me-2"></i> Órdenes de Trabajo (Taller)
                        <span class="badge bg-info text-dark">Técnico</span>
                    </a>

                    <%-- === GRUPO 5: ADMINISTRACIÓN (UsuarioServlet) === --%>
                    <% if (permisos.contains("GESTION_USUARIOS")) { %>
                    <a href="<%= request.getContextPath() %>/app/admin/usuarios"
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                        <i class="bi bi-person-gear me-2"></i> Gestión de Usuarios
                        <span class="badge bg-primary-light rounded-pill">Admin</span>
                    </a>

                    <a href="<%= request.getContextPath() %>/app/admin/roles"
                       class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                        <i class="bi bi-shield-lock me-2"></i> Roles y Permisos
                        <span class="badge bg-primary-light rounded-pill">Admin</span>
                    </a>
                    <% } %>

                </div>
            </div>
        </div>

        <div class="col-md-5">
            <div class="card shadow-sm">
                <div class="card-header bg-white">
                    <h3 class="h4 mb-0 text-accent"><i class="bi bi-shield-lock-fill me-2"></i> Tus Permisos Asignados</h3>
                </div>

                <ul class="list-group list-group-flush">
                    <% if (permisos.isEmpty()) { %>
                    <li class="list-group-item text-danger">No se encontraron permisos asignados.</li>
                    <% } else { %>
                    <% for (String permiso : permisos) { %>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <%= permiso %>
                        <i class="bi bi-check-circle-fill text-success"></i>
                    </li>
                    <% } %>
                    <% } %>
                </ul>

            </div>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>