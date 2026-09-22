package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/banco_digital";
    private static final String USER = "root";
    private static final String PASS = "Perrotta7."; // <-- ALTERE PARA SUA SENHA

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}