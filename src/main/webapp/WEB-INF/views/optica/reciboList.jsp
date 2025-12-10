<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 17:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Recibos/Ventas</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">💰 Recibos / Ventas</h1>

    <c:if test="${sessionScope.mensajeRecibo != null}">
        <div class="alert alert-info" role="alert">${sessionScope.mensajeRecibo}</div>
        <% session.removeAttribute("mensajeRecibo"); %>
    </c:if>

    <p>
        <a href="<c:url value="/app/ventas/recibos/new"/>" class="btn btn-primary">
            ➕ Nueva Venta/Recibo
        </a>
    </p>

    <table class="table table-hover">
        <thead class="bg-primary text-white">
        <tr>
            <th>Recibo #</th>
            <th>Fecha</th>
            <th>Cliente</th>
            <th>Total</th>
            <th>Saldo</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="recibo" items="${listaRecibos}">
            <tr>
                <td>${recibo.idRecibo}</td>
                <td><fmt:formatDate value="${recibo.fechaEmision}" pattern="dd/MM/yyyy"/></td>
                <td>${recibo.idCliente}</td> <td><fmt:formatNumber value="${recibo.total}" type="currency" currencySymbol="$"/></td>
                <td class="${recibo.saldo > 0 ? 'text-danger fw-bold' : ''}">
                    <fmt:formatNumber value="${recibo.saldo}" type="currency" currencySymbol="$"/>
                </td>
                <td>
                    <span class="badge bg-info">${recibo.estado}</span>
                </td>
                <td>
                    <a href="<c:url value="/app/ventas/recibos/view?id=${recibo.idRecibo}"/>" class="btn btn-sm btn-info">Ver</a>
                    <c:if test="${recibo.saldo > 0}">
                        <a href="<c:url value="/app/ventas/recibos/abonar?id=${recibo.idRecibo}"/>" class="btn btn-sm btn-success">Abonar</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
