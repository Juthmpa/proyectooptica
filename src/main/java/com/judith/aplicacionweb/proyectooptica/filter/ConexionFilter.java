package com.judith.aplicacionweb.proyectooptica.filter;

import com.judith.aplicacionweb.proyectooptica.dao.ConexionBD;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.sql.Connection;

@WebFilter("/*")
public class ConexionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try (Connection conn = ConexionBD.getConnection()) {

            if (conn == null || conn.isClosed()) {
                throw new ServletException("No hay conexión con la base de datos.");
            }

            chain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println(">>> [ConexionFilter] Error de conexión BD: " + e.getMessage());
            request.setAttribute("errorBD", "Error conectando con la base de datos.");
            request.getRequestDispatcher("/errorBD.jsp").forward(request, response);
        }
    }
}
