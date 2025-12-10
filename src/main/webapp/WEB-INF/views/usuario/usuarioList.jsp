<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 09:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Usuarios - Óptica Joesva</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        :root {
            --color-primary: #44B4EB;
            --color-success: #14A47C;
            --color-danger: #dc3545;
        }
        .bg-primary-custom {
            background-color: var(--color-primary) !important;
        }
        .text-status-active { color: var(--color-success); font-weight: bold; }
        .text-status-inactive { color: var(--color-danger); font-weight: bold; }
    </style>
</head>
<body class="bg-light">

<div class="container-fluid bg-primary-custom py-3 mb-4">
    <div class="container">
        <h1 class="text-white">👤 Módulo: Gestión de Usuarios</h1>
    </div>
</div>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Listado de Cuentas</h2>
        <c:if test="${usuarioSesion.tienePermiso('GESTION_USUARIOS')}">
            <a href="<%= request.getContextPath() %>/app/admin/usuarios/new" class="btn btn-success">
                <i class="fas fa-plus-circle"></i> Crear Nuevo Usuario
            </a>
        </c:if>
    </div>

    <%-- Mostrar mensaje de confirmación si existe --%>
    <c:if test="${not empty requestScope.mensaje}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <c:out value="${requestScope.mensaje}" />
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="table-responsive bg-white rounded shadow-sm p-3">
        <table class="table table-hover align-middle">
            <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Nombre Completo</th>
                <th>Rol</th>
                <th>Correo</th>
                <th>Teléfono</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <%-- Iterar sobre la lista de usuarios proporcionada por el Servlet (listaUsuarios) --%>
            <c:forEach var="usuario" items="${listaUsuarios}">
                <tr>
                    <td><c:out value="${usuario.id}" /></td>
                    <td><c:out value="${usuario.username}" /></td>
                    <td><c:out value="${usuario.nombreCompleto}" /></td>
                    <td><c:out value="${usuario.rol.nombre}" /></td>
                    <td><c:out value="${usuario.correo}" /></td>
                    <td><c:out value="${usuario.telefono}" /></td>
                    <td>
                        <c:choose>
                            <c:when test="${usuario.activo}">
                                <span class="text-status-active">Activo</span>
                            </c:when>
                            <c:otherwise>
                                <span class="text-status-inactive">Inactivo</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                            <%-- Botón Editar --%>
                        <c:if test="${usuarioSesion.tienePermiso('GESTION_USUARIOS')}">
                            <a href="<%= request.getContextPath() %>/app/admin/usuarios/edit?id=<c:out value='${usuario.id}' />"
                               class="btn btn-sm btn-info text-white me-2" title="Editar">
                                <i class="fas fa-edit"></i>
                            </a>
                        </c:if>

                            <%-- Botón de Cambio de Estado --%>
                        <c:choose>
                            <c:when test="${usuario.activo}">
                                <%-- Desactivar --%>
                                <c:if test="${usuarioSesion.tienePermiso('CAMBIAR_ESTADO_USUARIO')}">
                                    <a href="<%= request.getContextPath() %>/app/admin/usuarios/estado?id=<c:out value='${usuario.id}' />&estado=false"
                                       class="btn btn-sm btn-danger" title="Desactivar"
                                       onclick="return confirm('¿Está seguro de desactivar al usuario <c:out value='${usuario.username}' />?');">
                                        <i class="fas fa-user-slash"></i>
                                    </a>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <%-- Activar --%>
                                <c:if test="${usuarioSesion.tienePermiso('CAMBIAR_ESTADO_USUARIO')}">
                                    <a href="<%= request.getContextPath() %>/app/admin/usuarios/estado?id=<c:out value='${usuario.id}' />&estado=true"
                                       class="btn btn-sm btn-success" title="Activar"
                                       onclick="return confirm('¿Está seguro de reactivar al usuario <c:out value='${usuario.username}' />?');">
                                        <i class="fas fa-user-check"></i>
                                    </a>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <c:if test="${empty listaUsuarios}">
            <div class="alert alert-warning text-center" role="alert">
                No se encontraron usuarios registrados.
            </div>
        </c:if>

    </div>

    <div class="text-center mt-4">
        <a href="<%= request.getContextPath() %>/WEB-INF/index.jsp" class="btn btn-outline-secondary">
            <i class="fas fa-home"></i> Volver al Dashboard
        </a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
