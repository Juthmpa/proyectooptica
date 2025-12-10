<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Optometristas</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">🧑‍⚕️ Optometristas</h1>

    <c:if test="${sessionScope.mensajeOptometrista != null}">
        <div class="alert alert-info" role="alert">${sessionScope.mensajeOptometrista}</div>
        <% session.removeAttribute("mensajeOptometrista"); %>
    </c:if>

    <p>
        <a href="<c:url value="/app/gestion/optometristas/new"/>" class="btn btn-primary">
            ➕ Nuevo Optometrista
        </a>
    </p>

    <table class="table table-hover">
        <thead class="bg-primary text-white">
        <tr>
            <th>ID</th>
            <th>Nombres</th>
            <th>Especialidad</th>
            <th>Teléfono</th>
            <th>Correo</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="opto" items="${listaOptometristas}">
            <tr>
                <td>${opto.idOptometrista}</td>
                <td>${opto.nombres} ${opto.apellidos}</td>
                <td>${opto.especialidad}</td>
                <td>${opto.telefono}</td>
                <td>${opto.correo}</td>
                <td>
                    <a href="<c:url value="/app/gestion/optometristas/edit?id=${opto.idOptometrista}"/>" class="btn btn-sm btn-info">Editar</a>
                    <a href="<c:url value="/app/gestion/optometristas/delete?id=${opto.idOptometrista}"/>" class="btn btn-sm btn-danger" onclick="return confirm('¿Confirma eliminación física?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>