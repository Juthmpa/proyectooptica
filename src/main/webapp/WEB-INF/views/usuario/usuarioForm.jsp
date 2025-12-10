<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 8/12/2025
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${usuario.id == null ? 'Crear Nuevo Usuario' : 'Editar Usuario'}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --color-primary: #44B4EB; /* Azul */
            --color-success: #14A47C; /* Verde */
        }
        .btn-primary, .text-primary {
            background-color: var(--color-primary) !important;
            border-color: var(--color-primary) !important;
        }
        .btn-primary:hover {
            background-color: #3895c9 !important;
            border-color: #3895c9 !important;
        }
    </style>
</head>
<body class="bg-light">
<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow-lg border-0">
                <div class="card-header bg-white border-0 py-3">
                    <h2 class="text-center text-primary h3">
                        <c:out value="${usuario.id == null ? 'Creación de Usuario' : 'Edición de Usuario'}" />
                    </h2>
                </div>
                <div class="card-body p-4">

                    <form action="<%= request.getContextPath() %>/app/admin/usuarios/save" method="post">
                        <c:if test="${usuario.id != null}">
                            <input type="hidden" name="idUsuario" value="${usuario.id}" />
                            <div class="alert alert-info" role="alert">
                                Deje el campo de Contraseña vacío si no desea modificarla.
                            </div>
                        </c:if>

                        <div class="mb-3">
                            <label for="username" class="form-label">Nombre de Usuario (Username):</label>
                            <input type="text" id="username" name="username" class="form-control"
                                   value="${usuario.username}" required minlength="4"
                                   <c:if test="${usuario.id != null}">readonly</c:if> />
                        </div>

                        <div class="mb-3">
                            <label for="nombre" class="form-label">Nombre:</label>
                            <input type="text" id="nombre" name="nombre" class="form-control"
                                   value="${usuario.nombre}" required />
                        </div>

                        <div class="mb-3">
                            <label for="apellido" class="form-label">Apellido:</label>
                            <input type="text" id="apellido" name="apellido" class="form-control"
                                   value="${usuario.apellido}" required />
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">Contraseña:</label>
                            <input type="password" id="password" name="password" class="form-control"
                                   <c:if test="${usuario.id == null}">required</c:if> />
                        </div>

                        <div class="mb-4">
                            <label for="idRol" class="form-label">Rol:</label>
                            <select id="idRol" name="idRol" class="form-select" required>
                                <option value="">Seleccione un Rol</option>
                                <%-- Suponemos que el controlador pasa una lista llamada 'listaRoles' --%>
                                <c:forEach var="rol" items="${roles}">
                                    <option value="${rol.idRol}"
                                            <c:if test="${usuario.rol.idRol == rol.idRol}">selected</c:if>>
                                        <c:out value="${rol.nombre}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <c:out value="${usuario.id == null ? 'Crear Usuario' : 'Guardar Cambios'}" />
                            </button>
                            <a href="<%= request.getContextPath() %>/app/admin/usuarios" class="btn btn-secondary mt-2">Cancelar</a>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
