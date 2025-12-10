<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 17:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="accion" value="${recibo != null ? 'Actualizar' : 'Registrar'}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${accion} Recibo/Venta</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">${accion} Recibo</h1>

    <form action="<c:url value="/app/ventas/recibos/save"/>" method="post">
        <input type="hidden" name="idRecibo" value="${recibo.idRecibo}"/>
        <input type="hidden" name="idUsuario" value="${sessionScope.usuarioAutenticado.idUsuario}"/>

        <fieldset class="border p-3 mb-4">
            <legend class="float-none w-auto px-3">Datos de la Venta</legend>

            <div class="row g-3">
                <div class="col-md-6">
                    <label for="idCliente" class="form-label">Cliente (*)</label>
                    <select name="idCliente" id="idCliente" class="form-select" required>
                        <option value="">-- Seleccione Cliente --</option>
                        <option value="1" ${recibo.idCliente == 1 ? 'selected' : ''}>Juan Pérez</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label for="idFormaPago" class="form-label">Forma de Pago (*)</label>
                    <select name="idFormaPago" id="idFormaPago" class="form-select" required>
                        <option value="">-- Seleccione Forma --</option>
                        <option value="1" ${recibo.idFormaPago == 1 ? 'selected' : ''}>Efectivo</option>
                        <option value="2" ${recibo.idFormaPago == 2 ? 'selected' : ''}>Tarjeta</option>
                        <option value="3" ${recibo.idFormaPago == 3 ? 'selected' : ''}>Crédito</option>
                    </select>
                </div>
            </div>
        </fieldset>

        <fieldset class="border p-3 mb-4">
            <legend class="float-none w-auto px-3">Detalle de Productos</legend>

            <table class="table table-bordered" id="detalleTable">
                <thead>
                <tr>
                    <th>Producto</th>
                    <th style="width: 100px;">Cantidad</th>
                    <th style="width: 150px;">Precio Unitario</th>
                    <th style="width: 150px;">Subtotal</th>
                    <th style="width: 50px;"></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <select name="detalle[0].idProducto" class="form-select producto-select" required>
                            <option value="">-- Seleccione Producto --</option>
                            <option value="1">Lentes Bifocales</option>
                        </select>
                    </td>
                    <td><input type="number" name="detalle[0].cantidad" class="form-control cantidad-input" min="1" value="1" onchange="calcularTotal()"></td>
                    <td><input type="number" step="0.01" name="detalle[0].precioUnitario" class="form-control precio-input" value="150.00" onchange="calcularTotal()"></td>
                    <td class="subtotal-cell">150.00</td>
                    <td><button type="button" class="btn btn-danger btn-sm" onclick="eliminarFila(this)">X</button></td>
                </tr>
                </tbody>
            </table>
            <button type="button" class="btn btn-info btn-sm" onclick="agregarFila()">Añadir Producto</button>
        </fieldset>

        <fieldset class="border p-3 mb-4">
            <legend class="float-none w-auto px-3">Totales y Pagos</legend>
            <div class="row g-3">
                <div class="col-md-6">
                    <p class="h4">Total Venta: <span id="totalVentaDisplay">$0.00</span></p>
                    <input type="hidden" name="total" id="totalVentaInput" value="${recibo.total}">
                </div>
                <div class="col-md-3">
                    <label for="abono" class="form-label">Abono Inicial (*)</label>
                    <input type="number" step="0.01" name="abono" id="abono" class="form-control" value="${recibo.abono}" required onchange="calcularSaldo()">
                </div>
                <div class="col-md-3">
                    <p class="h4 mt-4">Saldo: <span id="saldoDisplay">$0.00</span></p>
                    <input type="hidden" name="saldo" id="saldoInput" value="${recibo.saldo}">
                </div>
            </div>
        </fieldset>

        <div class="d-flex justify-content-between">
            <a href="<c:url value="/app/ventas/recibos"/>" class="btn btn-secondary">Cancelar</a>
            <button type="submit" class="btn btn-success">${accion}</button>
        </div>
    </form>

    <script>
        function calcularTotal() {
            // Lógica de cálculo (Subtotal, Total, Saldo)
        }
        function calcularSaldo() {
            // Lógica para calcular Saldo = Total - Abono
        }
        function agregarFila() {
            // Lógica para añadir una nueva fila al detalle de productos
        }
        // Ejecutar al cargar
        document.addEventListener('DOMContentLoaded', function() {
            calcularTotal();
        });
    </script>
</div>
</body>
</html>
