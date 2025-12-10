<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 17:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="accion" value="${ot != null ? 'Actualizar' : 'Registrar'}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${accion} Orden de Trabajo</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">${accion} Orden de Trabajo</h1>

    <form action="<c:url value="/app/taller/ordenes/save"/>" method="post">
        <input type="hidden" name="idOrden" value="${ot.idOrden}"/>

        <div class="row g-3">
            <div class="col-md-4">
                <label for="numeroOt" class="form-label">Número OT</label>
                <input type="text" name="numeroOt" id="numeroOt" class="form-control" value="${ot.numeroOt}" readonly>
            </div>
            <div class="col-md-4">
                <label for="idCliente" class="form-label">Cliente (*)</label>
                <select name="idCliente" id="idCliente" class="form-select" required>
                    <option value="">-- Seleccione Cliente --</option>
                    <option value="1" ${ot.idCliente == 1 ? 'selected' : ''}>Juan Pérez</option>
                </select>
            </div>
            <div class="col-md-4">
                <label for="idTecnico" class="form-label">Técnico Asignado</label>
                <select name="idTecnico" id="idTecnico" class="form-select">
                    <option value="">-- Sin Asignar --</option>
                    <option value="1" ${ot.idTecnico == 1 ? 'selected' : ''}>Técnico A</option>
                </select>
            </div>

            <div class="col-md-4">
                <label for="fechaEntrega" class="form-label">Fecha Entrega Estimada</label>
                <input type="date" name="fechaEntrega" id="fechaEntrega" class="form-control" value="${ot.fechaEntrega}">
            </div>
            <div class="col-md-4">
                <label for="idEstadoOt" class="form-label">Estado OT (*)</label>
                <select name="idEstadoOt" id="idEstadoOt" class="form-select" required>
                    <option value="1" ${ot.idEstadoOt == 1 ? 'selected' : ''}>Pendiente</option>
                    <option value="2" ${ot.idEstadoOt == 2 ? 'selected' : ''}>En Proceso</option>
                    <option value="3" ${ot.idEstadoOt == 3 ? 'selected' : ''}>Finalizada</option>
                </select>
            </div>
            <div class="col-md-4">
                <label for="idTipoOt" class="form-label">Tipo OT (*)</label>
                <select name="idTipoOt" id="idTipoOt" class="form-select" required>
                    <option value="1" ${ot.idTipoOt == 1 ? 'selected' : ''}>Montura Propia</option>
                    <option value="2" ${ot.idTipoOt == 2 ? 'selected' : ''}>Reparación</option>
                </select>
            </div>

            <div class="col-12">
                <label for="observaciones" class="form-label">Observaciones/Instrucciones</label>
                <textarea name="observaciones" id="observaciones" class="form-control" rows="3">${ot.observaciones}</textarea>
            </div>

            <div class="col-12 mt-4 d-flex justify-content-between">
                <a href="<c:url value="/app/taller/ordenes"/>" class="btn btn-secondary">Cancelar</a>
                <button type="submit" class="btn btn-success">${accion}</button>
            </div>
        </div>
    </form>
</div>
</body>
</html>
