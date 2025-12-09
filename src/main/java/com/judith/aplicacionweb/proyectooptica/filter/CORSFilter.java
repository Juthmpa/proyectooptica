package com.judith.aplicacionweb.proyectooptica.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro CORS (Cross-Origin Resource Sharing).
 * Necesario para permitir que el frontend (ej. React/Vue en puerto 3000)
 * acceda a esta API (ej. WildFly en puerto 8080).
 */
@WebFilter("/*")
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización, si es necesaria.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 1. Permitir acceso desde cualquier origen (o el dominio de tu frontend en producción).
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");

        // 2. Permitir los métodos HTTP que usará la API.
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // 3. Permitir los encabezados personalizados (como X-User-Id para la simulación de rol).
        httpResponse.setHeader("Access-Control-Allow-Headers",
                "Content-Type, X-User-Id, X-Requested-With, accept, Authorization, origin");

        // 4. Manejar la petición OPTIONS (preflight request).
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Continuar con el siguiente filtro o con el Servlet de destino.
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Limpieza, si es necesaria.
    }
}