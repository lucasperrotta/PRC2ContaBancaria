package dao;

import model.ContaCorrente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {

    // 1. Inserir
    public void inserir(ContaCorrente conta) throws SQLException {
        String sql = "INSERT INTO contas (numero, titular, saldo) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, conta.getNumero());
            stmt.setString(2, conta.getTitular());
            stmt.setDouble(3, conta.getSaldo());
            stmt.executeUpdate();
        }
    }

    // 2. Listar todas
    public List<ContaCorrente> listar() throws SQLException {
        List<ContaCorrente> contas = new ArrayList<>();
        String sql = "SELECT numero, titular, saldo FROM contas";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                contas.add(new ContaCorrente(
                    rs.getInt("numero"), 
                    rs.getString("titular"), 
                    rs.getDouble("saldo")
                ));
            }
        }
        return contas;
    }

    // 3. Buscar por número
    public ContaCorrente buscarPorNumero(int numero) throws SQLException {
        String sql = "SELECT numero, titular, saldo FROM contas WHERE numero = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new ContaCorrente(
                    rs.getInt("numero"), 
                    rs.getString("titular"), 
                    rs.getDouble("saldo")
                );
            }
        }
        return null;
    }

    // 4. Atualizar Saldo
    public void atualizarSaldo(int numero, double novoSaldo) throws SQLException {
        String sql = "UPDATE contas SET saldo = ? WHERE numero = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, novoSaldo);
            stmt.setInt(2, numero);
            stmt.executeUpdate();
        }
    }

    // 5. Remover
    public void remover(int numero) throws SQLException {
        String sql = "DELETE FROM contas WHERE numero = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, numero);
            stmt.executeUpdate();
        }
    }
}