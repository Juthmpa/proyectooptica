<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 9/12/2025
  Time: 06:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bienvenido - Sistema Óptica</title>

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f0f0; /* GRIS SUAVE */
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            color: #333;
        }

        .card {
            background-color: #ffffff;
            padding: 40px 60px;
            border-radius: 12px;
            box-shadow: 0 4px 14px rgba(0,0,0,0.1);
            text-align: center;
            max-width: 450px;
        }

        h1 {
            color: #188C7C; /* Verde principal */
            margin-bottom: 10px;
        }

        p {
            font-size: 1.1em;
            color: #555;
            margin-bottom: 30px;
        }

        .btn-login {
            display: inline-block;
            background-color: #30A09C; /* Verde secundario */
            color: white;
            padding: 12px 30px;
            border-radius: 8px;
            font-size: 1.1em;
            text-decoration: none;
            font-weight: bold;
            transition: 0.3s;
        }

        .btn-login:hover {
            background-color: #188C7C; /* Verde principal */
        }
    </style>

</head>
<body>

<div class="card">
    <h1>Bienvenido</h1>
    <p>Sistema de Gestión para Óptica Joesva</p>

    <!-- ENLACE AL LOGIN -->
    <a href="login" class="btn-login">Iniciar Sesión</a>
</div>

</body>
</html>

