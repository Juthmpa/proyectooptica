package com.judith.aplicacionweb.proyectooptica.controller;
/*
 * Autor: Judith Piedra
 * Fecha: 04/12/2025
 * Descripción: Esta clase denominada LoginServlet,
 * modela el inicio de sesión.
 *
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.judith.aplicacionweb.proyectooptica.dao.UsuarioDAO;
import com.judith.aplicacionweb.proyectooptica.model.Usuario;

import java.io.IOException;

@WebServlet(name = "LoginController", urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;

    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            // Cierra la sesión y redirige al login
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            // Muestra la página de login
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Autenticar al usuario
            Usuario usuario = usuarioDAO.autenticar(username, password);

            if (usuario != null && usuario.getActivo()) { // Verificar que está activo

                // Éxito: Crear la sesión y almacenar datos de seguridad
                HttpSession session = request.getSession(true);
                // Renombrar a "usuarioSesion" para coincidir con index.jsp
                session.setAttribute("usuarioSesion", usuario);
                session.setAttribute("username", usuario.getUsername());
                // Permisos ya están dentro del objeto Usuario
                // session.setAttribute("permisosUsuario", usuario.getCodigosPermiso());

                // Redirigir al dashboard (index.jsp)
                response.sendRedirect(request.getContextPath() + "/dashboard.jsp");

            } else {
                // Fallo: Usuario no encontrado o inactivo
                request.setAttribute("error", "Credenciales inválidas o cuenta inactiva.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error del sistema: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}