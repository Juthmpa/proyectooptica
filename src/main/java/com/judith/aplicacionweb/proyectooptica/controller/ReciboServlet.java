package com.judith.aplicacionweb.proyectooptica.controller;

import com.judith.aplicacionweb.proyectooptica.dao.ReciboDAO;
import com.judith.aplicacionweb.proyectooptica.model.Recibo;
import com.judith.aplicacionweb.proyectooptica.model.DetalleRecibo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList; // Necesario para manejar el detalle

@WebServlet(name = "ReciboServlet", urlPatterns = {"/app/gestion/recibos", "/app/gestion/recibos/*"})
public class ReciboServlet extends HttpServlet {
    private ReciboDAO reciboDAO;
    // Necesitarás DAOs para Cliente, Usuario, FormaPago y Producto
    // private ClienteDAO clienteDAO;
    // private ProductoDAO productoDAO;

    @Override
    public void init() {
        reciboDAO = new ReciboDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getPathInfo() != null && request.getPathInfo().equals("/save")) {
            createReciboWithDetails(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/gestion/recibos");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getPathInfo() == null ? "" : request.getPathInfo();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/details":
                    showReciboDetails(request, response);
                    break;
                case "/anular": // Simula el cambio de estado (anular)
                    changeReciboStatus(request, response);
                    break;
                case "":
                default:
                    listRecibos(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("Error en el controlador de Recibos", ex);
        }
    }

    private void listRecibos(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<Recibo> listaRecibos = reciboDAO.listarTodos();
        request.setAttribute("listaRecibos", listaRecibos);
        request.getRequestDispatcher("/WEB-INF/views/gestion/recibo/reciboList.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Cargar lista de Clientes, Formas de Pago, Productos
        request.getRequestDispatcher("/WEB-INF/views/gestion/recibo/reciboForm.jsp").forward(request, response);
    }

    private void showReciboDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Recibo recibo = reciboDAO.obtenerPorId(id);
            // List<DetalleRecibo> detalles = reciboDAO.obtenerDetalles(id); // Asumimos un método en el DAO

            request.setAttribute("recibo", recibo);
            // request.setAttribute("detalles", detalles);
            request.getRequestDispatcher("/WEB-INF/views/gestion/recibo/reciboDetails.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de recibo inválido.");
        }
    }

    /**
     * Procesa la creación del recibo y sus detalles en una transacción.
     */
    private void createReciboWithDetails(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Este método debe ser implementado con una transacción segura en el DAO.
        // 1. Obtener datos del encabezado (Recibo)
        Long idCliente = Long.parseLong(request.getParameter("idCliente"));
        Long idFormaPago = Long.parseLong(request.getParameter("idFormaPago"));
        // ... obtener total, abono, saldo (calculados en el front/back)

        Recibo recibo = new Recibo();
        recibo.setIdCliente(idCliente);
        recibo.setIdFormaPago(idFormaPago);
        // ... setear total, abono, saldo, idUsuario

        // 2. Obtener datos del detalle (DetalleRecibo) - Vienen como arrays de parámetros
        List<DetalleRecibo> detalles = new ArrayList<>();
        String[] productoIds = request.getParameterValues("idProducto");
        String[] cantidades = request.getParameterValues("cantidad");
        String[] precios = request.getParameterValues("precioUnitario");

        if (productoIds != null) {
            for (int i = 0; i < productoIds.length; i++) {
                DetalleRecibo det = new DetalleRecibo();
                det.setIdProducto(Long.parseLong(productoIds[i]));
                det.setCantidad(Integer.parseInt(cantidades[i]));
                det.setPrecioUnitario(new BigDecimal(precios[i]));
                // Calcular subtotal...
                detalles.add(det);
            }
        }

        String mensaje = "";
        try {
            // Llama a un método en el DAO que maneje la transacción (Insertar Recibo y Detalles)
            // reciboDAO.insertarReciboCompleto(recibo, detalles);
            mensaje = "Recibo creado exitosamente.";
        } catch (Exception e) {
            mensaje = "Error al crear el recibo: " + e.getMessage();
        }

        request.getSession().setAttribute("mensajeRecibo", mensaje);
        response.sendRedirect(request.getContextPath() + "/app/gestion/recibos");
    }

    private void changeReciboStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String nuevoEstado = request.getParameter("estado"); // Ej: 'ANULADO', 'PAGADO'

            // reciboDAO.cambiarEstado(id, nuevoEstado);
            request.getSession().setAttribute("mensajeRecibo", "Estado del recibo cambiado a " + nuevoEstado + ".");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("mensajeRecibo", "Error: ID de recibo inválido.");
        }
        response.sendRedirect(request.getContextPath() + "/app/gestion/recibos");
    }
}
