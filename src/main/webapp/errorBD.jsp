<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 06:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error de Base de Datos</title>
</head>
<body>
<h1>¡Error de Conexión!</h1>
<p>Hubo un problema al conectar con la base de datos.</p>
<p>Detalle: ${errorBD}</p>
<p>Por favor, verifique el estado de su servidor MySQL y las credenciales en 'application.properties'.</p>
</body>
</html>
