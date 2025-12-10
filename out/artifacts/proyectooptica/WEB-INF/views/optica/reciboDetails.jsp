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
    <title>Detalle de Recibo #${recibo.idRecibo}</title>
    <style>
        .saldo-pendiente {
            color: #dc3545; /* Rojo de Bootstrap para destacar el saldo */
            font-weight: bold;
        }
        .total-pagado {
            color: var(--bs-success); /* Verde para pagos */
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">🧾 Detalle de Recibo N° ${recibo.idRecibo}</h1>

    <c:if test="${recibo == null}">
        <div class="alert alert-warning">Recibo no encontrado.</div>
    </c:if>

    <c:if test="${recibo != null}">

        <div class="row mb-4 border p-3">
            <div class="col-md-6">
                <p><strong>Cliente:</strong> ${recibo.clienteNombre}</p>
                <p><strong>Vendedor:</strong> ${recibo.vendedorNombre}</p>
            </div>
            <div class="col-md-6 text-end">
                <p><strong>Fecha de Emisión:</strong> <fmt:formatDate value="${recibo.fechaEmision}" pattern="dd/MM/yyyy HH:mm"/></p>
                <p><strong>Estado:</strong> <span class="badge bg-info">${recibo.estado}</span></p>
            </div>
        </div>

        <h3 class="text-primary">Artículos Vendidos</h3>
        <table class="table table-striped table-bordered">
            <thead class="bg-secondary text-white">
            <tr>
                <th>#</th>
                <th>Descripción del Producto</th>
                <th class="text-center">Cantidad</th>
                <th class="text-end">Precio Unitario</th>
                <th class="text-end">Subtotal</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="detalle" items="${recibo.detalleItems}" varStatus="loop">
                <tr>
                    <td>${loop.index + 1}</td>
                    <td>${detalle.nombreProducto}</td>
                    <td class="text-center">${detalle.cantidad}</td>
                    <td class="text-end"><fmt:formatNumber value="${detalle.precioUnitario}" type="currency" currencySymbol="$"/></td>
                    <td class="text-end"><fmt:formatNumber value="${detalle.subtotal}" type="currency" currencySymbol="$"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div class="row justify-content-end">
            <div class="col-md-5">
                <ul class="list-group">
                    <li class="list-group-item d-flex justify-content-between">
                        <span>**SUBTOTAL:**</span>
                        <span><fmt:formatNumber value="${recibo.subtotal}" type="currency" currencySymbol="$"/></span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between">
                        <span>**IVA (${recibo.porcentajeIva} %):**</span>
                        <span><fmt:formatNumber value="${recibo.montoIva}" type="currency" currencySymbol="$"/></span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between list-group-item-primary" style="background-color: var(--bs-primary); color: white;">
                        <span class="fw-bold">**TOTAL VENTA:**</span>
                        <span class="fw-bold"><fmt:formatNumber value="${recibo.total}" type="currency" currencySymbol="$"/></span>
                    </li>
                </ul>
            </div>
        </div>

        <h3 class="text-primary mt-4">Resumen de Pagos</h3>
        <div class="row mb-4">
            <div class="col-md-5">
                <ul class="list-group">
                    <li class="list-group-item d-flex justify-content-between">
                        <span>Abonado hasta la fecha:</span>
                        <span class="total-pagado"><fmt:formatNumber value="${recibo.totalAbonado}" type="currency" currencySymbol="$"/></span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between">
                        <span>Forma de Pago Inicial:</span>
                        <span>${recibo.formaPagoInicial}</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between list-group-item-light">
                        <span>**SALDO PENDIENTE:**</span>
                        <span class="saldo-pendiente"><fmt:formatNumber value="${recibo.saldo}" type="currency" currencySymbol="$"/></span>
                    </li>
                </ul>
            </div>
        </div>

        <div class="mt-4">
            <c:if test="${recibo.saldo > 0}">
                <a href="<c:url value="/app/ventas/recibos/abonar?id=${recibo.idRecibo}"/>" class="btn btn-success me-2">
                    Realizar Abono
                </a>
            </c:if>
            <a href="<c:url value="/app/ventas/recibos"/>" class="btn btn-secondary">Volver a la Lista</a>
        </div>

    </c:if>
</div>
</body>
</html>
