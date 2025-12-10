package com.judith.aplicacionweb.proyectooptica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.InputStream;

import com.mysql.cj.jdbc.MysqlDataSource;

public class ConexionBD {

    private static MysqlDataSource dataSource;

    static {
        try {
            Properties props = new Properties();
            InputStream in = ConexionBD.class.getClassLoader()
                    .getResourceAsStream("application.properties");
            props.load(in);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String pass = props.getProperty("db.password");

            dataSource = new MysqlDataSource();
            dataSource.setURL(url);
            dataSource.setUser(user);
            dataSource.setPassword(pass);

            System.out.println(">>> [ConexionBD] Pool inicializado correctamente");

        } catch (Exception e) {
            System.err.println(">>> Error cargando configuración de BD: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}