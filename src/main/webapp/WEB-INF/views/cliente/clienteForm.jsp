<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 09:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${cliente.idCliente == null ? 'Registrar Nuevo Cliente' : 'Editar Cliente'}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --color-primary: #44B4EB;
            --color-secondary: #007bff;
        }
        .btn-primary, .text-primary {
            background-color: var(--color-secondary) !important;
            border-color: var(--color-secondary) !important;
        }
        .btn-primary:hover {
            background-color: #0056b3 !important;
            border-color: #0056b3 !important;
        }
    </style>
</head>
<body class="bg-light">
<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-9 col-lg-7">
            <div class="card shadow-lg border-0">
                <div class="card-header bg-white border-0 py-3">
                    <h2 class="text-center text-primary h3">
                        <i class="fas fa-user-plus me-2"></i>
                        <c:out value="${cliente.idCliente == null ? 'Registro de Nuevo Cliente' : 'Actualización de Cliente'}" />
                    </h2>
                </div>
                <div class="card-body p-4">

                    <form action="<%= request.getContextPath() %>/app/gestion/clientes/save" method="post">

                        <%-- Campo Oculto para ID (solo en edición) --%>
                        <c:if test="${cliente.idCliente != null}">
                            <input type="hidden" name="idCliente" value="${cliente.idCliente}" />
                        </c:if>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="nombres" class="form-label">Nombres:</label>
                                <input type="text" id="nombres" name="nombres" class="form-control"
                                       value="<c:out value='${cliente.nombres}' />" required />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="apellidos" class="form-label">Apellidos:</label>
                                <input type="text" id="apellidos" name="apellidos" class="form-control"
                                       value="<c:out value='${cliente.apellidos}' />" required />
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="cedula" class="form-label">Cédula / Identificación:</label>
                                <input type="text" id="cedula" name="cedula" class="form-control"
                                       value="<c:out value='${cliente.cedula}' />" />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="fechaNacimiento" class="form-label">Fecha de Nacimiento:</label>
                                <input type="date" id="fechaNacimiento" name="fechaNacimiento" class="form-control"
                                       value="<c:out value='${cliente.fechaNacimiento}' />" />
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="telefono" class="form-label">Teléfono:</label>
                                <input type="tel" id="telefono" name="telefono" class="form-control"
                                       value="<c:out value='${cliente.telefono}' />" />
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="correo" class="form-label">Correo Electrónico:</label>
                                <input type="email" id="correo" name="correo" class="form-control"
                                       value="<c:out value='${cliente.correo}' />" />
                            </div>
                        </div>

                        <div class="mb-4">
                            <label for="direccion" class="form-label">Dirección:</label>
                            <textarea id="direccion" name="direccion" class="form-control"><c:out value="${cliente.direccion}" /></textarea>
                        </div>

                        <div class="d-grid gap-2 mt-4">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="fas fa-save me-2"></i>
                                <c:out value="${cliente.idCliente == null ? 'Guardar Cliente' : 'Actualizar Datos'}" />
                            </button>
                            <a href="<%= request.getContextPath() %>/app/gestion/clientes" class="btn btn-secondary mt-2">
                                <i class="fas fa-times-circle me-2"></i> Cancelar
                            </a>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/js/all.min.js"></script>
</body>
</html>
