<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 8/12/2025
  Time: 09:03
  To change this template use File | Settings | File Templates.
--%>
<%-- /webapp/WEB-INF/views/optometria/valoracion/valoracionForm.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registro de Valoración y Prescripción</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/custom.css">
</head>
<body>
<h1>📝 Registrar Valoración</h1>
<form action="<%= request.getContextPath() %>/app/optometria/valoraciones/save" method="post">

    <h2>Datos Generales</h2>
    <label>Cliente:</label>
    <select name="idCliente" required>
        <%-- Aquí se iteraría sobre la lista de clientes cargada por el controlador --%>
        <option value="1">Ejemplo Cliente 1</option>
    </select><br><br>

    <label>Tipo Certificado:</label>
    <select name="idTipoCertificado" required>
        <%-- Aquí se iteraría sobre los tipos de certificado --%>
        <option value="1">Ejemplo Certificado 1</option>
    </select><br><br>

    <label>Observaciones de Valoración:</label><br>
    <textarea name="observacionesValoracion" rows="3" cols="50"></textarea><br><br>

    <h2>Prescripción Óptica</h2>
    <input type="checkbox" id="incluirPrescripcion" name="incluirPrescripcion" value="true" checked>
    <label for="incluirPrescripcion">Incluir Prescripción</label><br><br>

    <div id="prescripcion-fields">
        <p><strong>Ojo Derecho (OD):</strong></p>
        SPH: <input type="number" step="0.01" name="sphOd" value="0.00" required>
        CYL: <input type="number" step="0.01" name="cylOd" value="0.00" required>
        EJE: <input type="number" name="ejeOd" value="0" required><br><br>

        <p><strong>Ojo Izquierdo (OI):</strong></p>
        SPH: <input type="number" step="0.01" name="sphOi" value="0.00" required>
        CYL: <input type="number" step="0.01" name="cylOi" value="0.00" required>
        EJE: <input type="number" name="ejeOi" value="0" required><br><br>

        <label>Adición (Add):</label>
        <input type="number" step="0.01" name="adicion" value="0.00" required><br><br>

        <label>Tipo Prescripción:</label>
        <select name="tipoPrescripcionId" required>
            <%-- Aquí se iteraría sobre los tipos de prescripción --%>
            <option value="1">MONOFOCAL</option>
        </select><br><br>

        <label>Observaciones de Prescripción:</label><br>
        <textarea name="observacionesPrescripcion" rows="3" cols="50"></textarea><br><br>
    </div>

    <button type="submit">Guardar Valoración y Prescripción</button>
</form>

<script>
    // Lógica simple para ocultar/mostrar la sección de prescripción si el checkbox está desmarcado
    document.getElementById('incluirPrescripcion').addEventListener('change', function() {
        document.getElementById('prescripcion-fields').style.display = this.checked ? 'block' : 'none';
    });
</script>
</body>
</html>