<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Citas</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">📅 Agenda de Citas</h1>

    <c:if test="${sessionScope.mensajeCita != null}">
        <div class="alert alert-info" role="alert">${sessionScope.mensajeCita}</div>
        <% session.removeAttribute("mensajeCita"); %>
    </c:if>

    <p>
        <a href="<c:url value="/app/agenda/citas/new"/>" class="btn btn-primary">
            ➕ Agendar Cita
        </a>
    </p>

    <table class="table table-hover">
        <thead class="bg-primary text-white">
        <tr>
            <th>ID</th>
            <th>Fecha/Hora</th>
            <th>Cliente</th>
            <th>Optometrista</th>
            <th>Motivo</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="cita" items="${listaCitas}">
            <tr>
                <td>${cita.idCita}</td>
                <td><fmt:formatDate value="${cita.fechaHora}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td>${cita.idCliente}</td> <td>${cita.idOptometrista}</td> <td>${cita.motivo}</td>
                <td>
                    <span class="badge bg-info">${cita.idEstado}</span> </td>
                <td>
                    <a href="<c:url value="/app/agenda/citas/edit?id=${cita.idCita}"/>" class="btn btn-sm btn-info">Editar</a>
                    <a href="<c:url value="/app/agenda/citas/cancel?id=${cita.idCita}"/>" class="btn btn-sm btn-danger" onclick="return confirm('¿Desea CANCELAR esta cita?');">Cancelar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
