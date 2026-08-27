package org.example;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoBD {
    public static Connection conectar() throws SQLException {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));
            String senha = props.getProperty("senha");

            String url = "jdbc:mysql://localhost:3306/crud_app";
            String usuario = "root";
            return DriverManager.getConnection(url, usuario, senha);
        } catch (Exception e) {
            throw new SQLException("Erro ao ler configuração: " + e.getMessage());
        }
    }
}