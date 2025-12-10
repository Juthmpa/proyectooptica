<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 16:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detalle de Prescripción #${prescripcion.idPrescripcion}</title>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-primary">📋 Detalle de Prescripción #${prescripcion.idPrescripcion}</h1>

    <c:if test="${prescripcion == null}">
        <div class="alert alert-warning">Prescripción no encontrada.</div>
    </c:if>

    <c:if test="${prescripcion != null}">
        <div class="row mb-4">
            <div class="col-md-6">
                <p><strong>ID Valoración:</strong> ${prescripcion.idValoracion}</p>
                <p><strong>Optometrista:</strong> ${prescripcion.idOptometrista}</p>
            </div>
            <div class="col-md-6">
                <p><strong>Tipo de Prescripción:</strong> ${prescripcion.tipoPrescripcionId}</p>
                <p><strong>Fecha de Creación:</strong> ${prescripcion.fechaCreacion}</p>
            </div>
        </div>

        <h3 class="text-primary">Medidas (Fórmula Oftálmica)</h3>
        <table class="table table-bordered text-center w-75">
            <thead class="bg-secondary text-white">
            <tr>
                <th>Ojo</th>
                <th>SPH (Esfera)</th>
                <th>CYL (Cilindro)</th>
                <th>EJE</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>OD (Derecho)</td>
                <td>${prescripcion.sphOd}</td>
                <td>${prescripcion.cylOd}</td>
                <td>${prescripcion.ejeOd}</td>
            </tr>
            <tr>
                <td>OI (Izquierdo)</td>
                <td>${prescripcion.sphOi}</td>
                <td>${prescripcion.cylOi}</td>
                <td>${prescripcion.ejeOi}</td>
            </tr>
            </tbody>
        </table>

        <p class="lead mt-3"><strong>Adición:</strong> ${prescripcion.adicion}</p>

        <h4 class="text-primary">Observaciones</h4>
        <p class="alert alert-light">${prescripcion.observaciones}</p>

        <div class="mt-4">
            <a href="<c:url value="/app/optometria//app/optometria/prescripciones/edit?id=${prescripcion.idValoracion}"/>" class="btn btn-info">Editar Valoración Completa</a>
            <a href="<c:url value="/app/optometria/prescripciones"/>" class="btn btn-secondary">Volver a la Lista</a>
        </div>
    </c:if>
</div>
</body>
</html>
