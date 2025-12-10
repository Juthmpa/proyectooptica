<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 17:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="accion" value="${cita != null ? 'Actualizar' : 'Agendar'}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${accion} Cita</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">${accion} Cita</h1>

    <form action="<c:url value="/app/agenda/citas/save"/>" method="post">
        <input type="hidden" name="idCita" value="${cita.idCita}"/>

        <div class="row g-3">
            <div class="col-md-6">
                <label for="idCliente" class="form-label">Cliente (*)</label>
                <select name="idCliente" id="idCliente" class="form-select" required>
                    <option value="">-- Seleccione Cliente --</option>
                    <option value="1" ${cita.idCliente == 1 ? 'selected' : ''}>Juan Pérez</option>
                </select>
            </div>
            <div class="col-md-6">
                <label for="idOptometrista" class="form-label">Optometrista (*)</label>
                <select name="idOptometrista" id="idOptometrista" class="form-select" required>
                    <option value="">-- Seleccione Optometrista --</option>
                    <option value="1" ${cita.idOptometrista == 1 ? 'selected' : ''}>Dr. Smith</option>
                </select>
            </div>

            <div class="col-md-6">
                <label for="fechaHora" class="form-label">Fecha y Hora (*)</label>
                <input type="datetime-local" name="fechaHora" id="fechaHora" class="form-control"
                       value="${cita.fechaHora}" required>
            </div>
            <div class="col-md-6">
                <label for="idEstado" class="form-label">Estado (*)</label>
                <select name="idEstado" id="idEstado" class="form-select" required>
                    <option value="1" ${cita.idEstado == 1 ? 'selected' : ''}>Pendiente</option>
                    <option value="2" ${cita.idEstado == 2 ? 'selected' : ''}>Confirmada</option>
                </select>
            </div>

            <div class="col-12">
                <label for="motivo" class="form-label">Motivo de la Cita</label>
                <textarea name="motivo" id="motivo" class="form-control" rows="2">${cita.motivo}</textarea>
            </div>

            <div class="col-12 mt-4 d-flex justify-content-between">
                <a href="<c:url value="/app/agenda/citas"/>" class="btn btn-secondary">Cancelar</a>
                <button type="submit" class="btn btn-success">${accion}</button>
            </div>
        </div>
    </form>
</div>
</body>
</html>
