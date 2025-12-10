package com.judith.aplicacionweb.proyectooptica.filter;

import com.judith.aplicacionweb.proyectooptica.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebFilter("/app/*") // Se aplica a todas las rutas protegidas, incluido /app/dashboard

public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        HttpSession session = request.getSession(false);
        Usuario usuario = null;

        // 1. Comprobación de Autenticación
        if (session != null && session.getAttribute("usuarioSesion") != null) {
            usuario = (Usuario) session.getAttribute("usuarioSesion");
        }

        // Si el usuario no está autenticado, redirigir al login
        if (usuario == null) {
            System.out.println(">>> [SecurityFilter] Acceso denegado. Redirigiendo a Login.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2. Comprobación de Permisos (Ejemplo)
        Set<String> permisos = usuario.getCodigosPermiso();

        // Si la ruta es el Dashboard, solo se necesita estar autenticado (ya cubierto arriba)
        if (path.equals("/app/dashboard")) {
            chain.doFilter(req, res);
            return;
        }

        // 3. Comprobación de Permisos Específicos (para módulos)
        if (path.startsWith("/app/admin/usuarios")) {
            if (!permisos.contains("GESTION_USUARIOS")) {
                System.out.println(">>> [SecurityFilter] Permiso GESTION_USUARIOS denegado para " + usuario.getUsername());
                response.sendError(HttpServletResponse.SC_FORBIDDEN); // HTTP 403
                return;
            }
        }

        if (path.startsWith("/app/optometria/valoraciones")) {
            if (!permisos.contains("CREAR_VALORACION") && !permisos.contains("VER_VALORACION")) {
                System.out.println(">>> [SecurityFilter] Permiso CREAR_VALORACION o VER_VALORACION denegado.");
                response.sendError(HttpServletResponse.SC_FORBIDDEN); // HTTP 403
                return;
            }
        }

        // Si pasa todas las comprobaciones, continúa la cadena
        chain.doFilter(req, res);
    }
}