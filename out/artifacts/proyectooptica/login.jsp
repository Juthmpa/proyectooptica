<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 4/12/2025
  Time: 08:40
  To change this template use File | Settings | File Templates.
--%>
<%-- /login.jsp --%>
<%-- /webapp/login.jsp --%>
<%-- webapp/login.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Login - Óptica Joesva</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        :root {
            --color-primary: #44B4EB;
            --color-dark-green: #188C7C;
            --color-accent: #30A09C;
            --color-success: #14A47C;
        }

        body {
            background: #f1f1f1; /* Gris bajito */
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            font-family: "Segoe UI", sans-serif;
            padding: 20px;
        }

        .welcome-text {
            text-align: center;
            margin-bottom: 25px;
            animation: fadeIn 0.8s ease;
        }

        .welcome-text h1 {
            font-weight: 700;
            color: var(--color-dark-green);
        }

        .welcome-text p {
            font-size: 1.1rem;
            color: #555;
        }

        .login-card {
            width: 390px;
            border-radius: 16px;
            overflow: hidden;
            animation: fadeInUp 0.7s ease;
        }

        .login-header {
            background: var(--color-dark-green);
            color: #fff;
            text-align: center;
            padding: 25px 15px;
        }

        .login-header h2 {
            font-weight: 600;
            font-size: 1.7rem;
            margin: 0;
        }

        .card-body {
            padding: 28px;
            background: #fff;
        }

        .form-control:focus {
            border-color: var(--color-primary);
            box-shadow: 0 0 0 0.2rem rgba(68, 180, 235, 0.25);
        }

        .btn-login {
            background: var(--color-primary);
            border: none;
            font-size: 1.1rem;
            padding: 10px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-login:hover {
            background: var(--color-dark-green);
            transform: scale(1.02);
        }

        .error-message {
            text-align: center;
            color: #c0392b;
            font-weight: bold;
            margin-bottom: 15px;
            animation: fadeIn 0.4s ease;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(12px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
    </style>
</head>

<body>

<!-- Bienvenida -->
<div class="welcome-text">
    <h1>Bienvenido a Óptica Joesva</h1>
    <p>Accede al sistema para gestionar clientes, ventas y servicios.</p>
</div>

<!-- Tarjeta de Login -->
<div class="card login-card shadow-lg">
    <div class="login-header">
        <h2>Iniciar Sesión</h2>
    </div>

    <div class="card-body">

        <%-- Si hay error, mostrar mensaje simple sin rectángulo rojo
        Usando el atributo 'error' que envía el Servlet --%>
        <c:if test="${not empty requestScope.error}">
            <div class="error-message">
                <c:out value="${requestScope.error}"/>
            </div>
        </c:if>

        <form method="POST" action="<%= request.getContextPath() %>/login">

            <div class="mb-3">
                <label for="username" class="form-label fw-semibold">Usuario</label>
                <input type="text" class="form-control" id="username" name="username" required>
            </div>

            <div class="mb-3">
                <label for="password" class="form-label fw-semibold">Contraseña</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>

            <button type="submit" class="btn btn-login w-100 mt-2">Ingresar</button>
        </form>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>