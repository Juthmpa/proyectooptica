package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.dao.RolDAO;
import com.judith.aplicacionweb.proyectooptica.dao.UsuarioDAO;
import com.judith.aplicacionweb.proyectooptica.model.Rol;
import com.judith.aplicacionweb.proyectooptica.model.Usuario;
import com.judith.aplicacionweb.proyectooptica.util.PasswordHashUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/app/admin/usuarios/*") // Rutas bajo /app/admin/usuarios/
public class UsuarioServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;
    private RolDAO rolDAO; // Se necesita una clase RolDAO para obtener roles disponibles

    public void init() {
        usuarioDAO = new UsuarioDAO();
        rolDAO = new RolDAO();
    }

    // Maneja CREATE y UPDATE (Vía Formulario)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Verifica si la acción es guardar (insertar o actualizar)
        if (pathInfo != null && pathInfo.equals("/save")) {
            try {
                insertOrUpdateUsuario(request, response);
            } catch (SQLException e) {
                // Manejo de error: se podría redirigir a una página de error con el mensaje
                throw new ServletException("Error al guardar usuario", e);
            }
        } else {
            // Si no es /save, se puede redirigir al listado o manejar como GET.
            doGet(request, response);
        }
    }

    // Maneja READ (Listar, Mostrar Formulario), y la acción de Estado (Vía URL)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo() == null ? "" : request.getPathInfo();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/estado": // Nuevo endpoint para cambiar estado
                    cambiarEstadoUsuario(request, response);
                    break;
                default:
                    listUsuarios(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("Error de base de datos en el controlador", ex);
        }
    }

    /**
     * Muestra la lista de todos los usuarios (incluyendo el estado).
     */
    private void listUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        //
        List<Usuario> listaUsuarios = usuarioDAO.seleccionarTodos();
        request.setAttribute("listaUsuarios", listaUsuarios);
        request.getRequestDispatcher("/WEB-INF/views/admin/usuario/usuarioList.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Necesita RolDAO para cargar los roles
        request.setAttribute("roles", rolDAO.seleccionarTodos());
        request.getRequestDispatcher("/WEB-INF/views/admin/usuario/usuarioForm.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Usuario existingUser = usuarioDAO.seleccionarPorId(id);

        // Cargar roles también para el dropdown
        request.setAttribute("roles", rolDAO.seleccionarTodos());

        request.setAttribute("usuario", existingUser);
        request.getRequestDispatcher("/WEB-INF/views/admin/usuario/usuarioForm.jsp").forward(request, response);
    }

    /**
     * Inserta un nuevo usuario o actualiza uno existente.
     */
    private void insertOrUpdateUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String idParam = request.getParameter("idUsuario");

        // 1. Recolección de datos
        Long idRol = Long.parseLong(request.getParameter("idRol"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // El estado 'activo' solo se recoge en el formulario de edición si se permite cambiarlo
        Boolean activo = "true".equalsIgnoreCase(request.getParameter("activo"));

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        // Aquí se aplica el HASHING antes de que el DAO lo guarde
        if (password != null && !password.isEmpty()) {
            // Solo hasheamos si se proporcionó una nueva contraseña
            usuario.setPasswordHash(PasswordHashUtil.hashPassword(password));
        }
        Rol rol = new Rol();
        rol.setIdRol(idRol);
        usuario.setRol(rol);

        if (idParam == null || idParam.isEmpty()) {
            // INSERTAR: El DAO setea activo=1 por defecto
            usuarioDAO.insertar(usuario);
        } else {
            // ACTUALIZAR
            usuario.setId(Long.parseLong(idParam));
            usuario.setActivo(activo); // Se actualiza el estado si vino del formulario
            usuarioDAO.actualizar(usuario);
        }

        response.sendRedirect(request.getContextPath() + "/app/admin/usuarios");
    }

    /**
     * Método CLAVE: Cambia el estado del usuario (Activar/Desactivar).
     * Ruta: /app/admin/usuarios/estado?id=X&estado=true/false
     */
    private void cambiarEstadoUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        // El nuevo estado se pasa como parámetro booleano en la URL
        boolean nuevoEstado = Boolean.parseBoolean(request.getParameter("estado"));

        usuarioDAO.cambiarEstado(id, nuevoEstado);

        // Redirigir de nuevo a la lista de usuarios
        response.sendRedirect(request.getContextPath() + "/app/admin/usuarios");
    }
}