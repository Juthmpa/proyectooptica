<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 17:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Órdenes de Trabajo</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">⚙️ Órdenes de Trabajo (OT)</h1>

    <c:if test="${sessionScope.mensajeOT != null}">
        <div class="alert alert-info" role="alert">${sessionScope.mensajeOT}</div>
        <% session.removeAttribute("mensajeOT"); %>
    </c:if>

    <p>
        <a href="<c:url value="/app/taller/ordenes/new"/>" class="btn btn-primary">
            ➕ Crear Nueva OT
        </a>
    </p>

    <table class="table table-hover">
        <thead class="bg-primary text-white">
        <tr>
            <th>OT #</th>
            <th>Cliente</th>
            <th>Ingreso</th>
            <th>Entrega Est.</th>
            <th>Tipo</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="ot" items="${listaOrdenesTrabajo}">
            <tr>
                <td>${ot.numeroOt}</td>
                <td>${ot.idCliente}</td> <td><fmt:formatDate value="${ot.fechaIngreso}" pattern="dd/MM/yyyy"/></td>
                <td><fmt:formatDate value="${ot.fechaEntrega}" pattern="dd/MM/yyyy"/></td>
                <td>${ot.idTipoOt}</td> <td>
                <span class="badge bg-info">${ot.idEstadoOt}</span> </td>
                <td>
                    <a href="<c:url value="/app/taller/ordenes/edit?id=${ot.idOrden}"/>" class="btn btn-sm btn-info">Editar</a>
                    <a href="<c:url value="/app/taller/ordenes/complete?id=${ot.idOrden}"/>" class="btn btn-sm btn-success">Finalizar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
