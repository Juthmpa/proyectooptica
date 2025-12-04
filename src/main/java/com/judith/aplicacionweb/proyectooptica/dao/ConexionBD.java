package com.judith.aplicacionweb.proyectooptica.dao;

import java.sql.Connection;
import java.sql.SQLException;
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
}