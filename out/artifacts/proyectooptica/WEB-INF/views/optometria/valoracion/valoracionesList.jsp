<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 8/12/2025
  Time: 09:03
  To change this template use File | Settings | File Templates.
--%>
<%-- /webapp/WEB-INF/views/valoracionesList.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Lista de Valoraciones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        :root {
            --color-primary: #44B4EB; /* Azul */
            --color-success: #14A47C; /* Verde */
        }
        .text-primary { color: var(--color-primary) !important; }
        .table-primary { background-color: var(--color-primary) !important; color: white; border-color: var(--color-primary); }
        .btn-success { background-color: var(--color-success) !important; border-color: var(--color-success) !important; }
    </style>
</head>
<body class="bg-light">

<div class="container my-5">
    <h1 class="mb-4 text-primary">📋 Historial de Valoraciones</h1>

    <div class="d-flex justify-content-end mb-4">
        <a href="<%= request.getContextPath() %>/app/valoraciones/new" class="btn btn-success">
            <i class="bi bi-plus-circle-fill me-2"></i> Crear Nueva Valoración
        </a>
    </div>

    <div class="card shadow-sm">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="table-primary">
                    <tr>
                        <th>ID</th>
                        <th>Fecha</th>
                        <th>Cliente</th>
                        <th>Optometrista</th>
                        <th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty listaValoraciones}">
                            <c:forEach var="valoracion" items="${listaValoraciones}">
                                <tr>
                                    <td>${valoracion.idValoracion}</td>
                                    <td>${valoracion.fechaValoracion}</td>
                                    <td>${valoracion.nombreCliente}</td>
                                    <td>${valoracion.nombreOptometrista}</td>
                                    <td>
                                        <a href="valoraciones/view?id=${valoracion.idValoracion}" class="btn btn-sm btn-outline-primary">
                                            <i class="bi bi-eye-fill"></i> Ver Detalle
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="text-center text-muted">No se encontraron valoraciones registradas.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>