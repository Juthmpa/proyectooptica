<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="accion" value="${opto != null ? 'Actualizar' : 'Registrar'}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${accion} Optometrista</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">${accion} Optometrista</h1>

    <form action="<c:url value="/app/gestion/optometristas/save"/>" method="post">
        <input type="hidden" name="idOptometrista" value="${opto.idOptometrista}"/>

        <div class="row g-3">
            <div class="col-md-6">
                <label for="nombres" class="form-label">Nombres (*)</label>
                <input type="text" name="nombres" id="nombres" class="form-control" value="${opto.nombres}" required>
            </div>
            <div class="col-md-6">
                <label for="apellidos" class="form-label">Apellidos (*)</label>
                <input type="text" name="apellidos" id="apellidos" class="form-control" value="${opto.apellidos}" required>
            </div>

            <div class="col-md-6">
                <label for="especialidad" class="form-label">Especialidad</label>
                <input type="text" name="especialidad" id="especialidad" class="form-control" value="${opto.especialidad}">
            </div>
            <div class="col-md-6">
                <label for="telefono" class="form-label">Teléfono</label>
                <input type="tel" name="telefono" id="telefono" class="form-control" value="${opto.telefono}">
            </div>

            <div class="col-12">
                <label for="correo" class="form-label">Correo (*)</label>
                <input type="email" name="correo" id="correo" class="form-control" value="${opto.correo}" required>
            </div>

            <div class="col-12 mt-4 d-flex justify-content-between">
                <a href="<c:url value="/app/gestion/optometristas"/>" class="btn btn-secondary">Cancelar</a>
                <button type="submit" class="btn btn-success">${accion}</button>
            </div>
        </div>
    </form>
</div>
</body>
</html>
