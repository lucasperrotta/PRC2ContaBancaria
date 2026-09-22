/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    // Ajuste a URL para o seu banco 'banco_digital'
    private static final String URL = "jdbc:mysql://localhost:3306/banco_digital";
    private static final String USER = "root";
    private static final String PASS = "Perrotta7."; // <-- COLOQUE SUA SENHA

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}