<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 09:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Clientes - Óptica Joesva</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        :root {
            --color-primary: #44B4EB;
            --color-secondary: #007bff;
        }
        .bg-primary-custom {
            background-color: var(--color-secondary) !important;
        }
    </style>
</head>
<body class="bg-light">

<div class="container-fluid bg-primary-custom py-3 mb-4">
    <div class="container">
        <h1 class="text-white">🧑‍🤝‍🧑 Módulo: Gestión de Clientes/Pacientes</h1>
    </div>
</div>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Listado de Clientes Activos</h2>
        <%-- Suponemos que el permiso para crear es 'CREAR_PACIENTE' --%>
        <c:if test="${usuarioSesion.tienePermiso('CREAR_PACIENTE')}">
            <a href="<%= request.getContextPath() %>/app/gestion/clientes/new" class="btn btn-success">
                <i class="fas fa-user-plus"></i> Registrar Nuevo Cliente
            </a>
        </c:if>
    </div>

    <%-- Mostrar mensaje de sesión si existe (usado para mensajes post-CRUD) --%>
    <c:if test="${not empty sessionScope.mensajeCliente}">
        <div class="alert alert-info alert-dismissible fade show" role="alert">
            <c:out value="${sessionScope.mensajeCliente}" />
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="mensajeCliente" scope="session" /> <%-- Limpiar la sesión --%>
    </c:if>

    <div class="table-responsive bg-white rounded shadow-sm p-3">
        <table class="table table-hover align-middle">
            <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Cédula</th>
                <th>Nombres</th>
                <th>Teléfono</th>
                <th>Correo</th>
                <th>F. Nacimiento</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="cliente" items="${listaClientes}">
                <tr>
                    <td><c:out value="${cliente.idCliente}" /></td>
                    <td><c:out value="${cliente.cedula}" /></td>
                    <td><c:out value="${cliente.nombres} ${cliente.apellidos}" /></td>
                    <td><c:out value="${cliente.telefono}" /></td>
                    <td><c:out value="${cliente.correo}" /></td>
                    <td>
                        <c:if test="${cliente.fechaNacimiento != null}">
                            <fmt:formatDate value="${cliente.fechaNacimiento}" pattern="dd/MM/yyyy" />
                        </c:if>
                    </td>
                    <td>
                            <%-- Botón Editar --%>
                        <c:if test="${usuarioSesion.tienePermiso('EDITAR_PACIENTE')}">
                            <a href="<%= request.getContextPath() %>/app/gestion/clientes/edit?id=<c:out value='${cliente.idCliente}' />"
                               class="btn btn-sm btn-info text-white me-2" title="Editar">
                                <i class="fas fa-edit"></i>
                            </a>
                        </c:if>

                            <%-- Botón Eliminar (Lógico) --%>
                        <c:if test="${usuarioSesion.tienePermiso('CAMBIAR_ESTADO_PACIENTE')}">
                            <a href="<%= request.getContextPath() %>/app/gestion/clientes/delete?id=<c:out value='${cliente.idCliente}' />"
                               class="btn btn-sm btn-danger" title="Eliminar (Desactivar)"
                               onclick="return confirm('¿Está seguro de desactivar al cliente <c:out value='${cliente.nombres}' />?');">
                                <i class="fas fa-trash"></i>
                            </a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <c:if test="${empty listaClientes}">
            <div class="alert alert-warning text-center" role="alert">
                No se encontraron clientes activos registrados.
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/js/all.min.js"></script>
</body>
</html>
