package dao;

import model.ContaCorrente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {

    // ✅ 1. INSERIR (PreparedStatement)
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

    // ✅ 2. LISTAR TODAS
    public List<ContaCorrente> listarTodas() throws SQLException {
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

    // ✅ 3. BUSCAR POR NÚMERO
    public ContaCorrente buscarPorNumero(int numero) throws SQLException {
        String sql = "SELECT numero, titular, saldo FROM contas WHERE numero = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new ContaCorrente(rs.getInt("numero"), rs.getString("titular"), rs.getDouble("saldo"));
            }
        }
        return null;
    }

    // ✅ 4. ATUALIZAR SALDO
    public void atualizarSaldo(int numero, double novoSaldo) throws SQLException {
        String sql = "UPDATE contas SET saldo = ? WHERE numero = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, novoSaldo);
            stmt.setInt(2, numero);
            stmt.executeUpdate();
        }
    }

    // 🌟 5. TRANSFERÊNCIA COM TRANSAÇÃO (O PONTO CHAVE DA AULA 06)
    public void transferir(int numeroOrigem, int numeroDestino, double valor) throws Exception {
        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            
            // 1. Desliga o auto-commit para iniciar a transação manual
            conn.setAutoCommit(false);

            // 2. Verifica se a conta de origem tem saldo suficiente
            ContaCorrente origem = buscarPorNumeroComConn(conn, numeroOrigem);
            if (origem == null) throw new Exception("Conta de origem não encontrada.");
            if (origem.getSaldo() < valor) throw new Exception("Saldo insuficiente na conta de origem.");

            // 3. Débito na origem
            String sqlDebito = "UPDATE contas SET saldo = saldo - ? WHERE numero = ?";
            try (PreparedStatement stmtDebito = conn.prepareStatement(sqlDebito)) {
                stmtDebito.setDouble(1, valor);
                stmtDebito.setInt(2, numeroOrigem);
                stmtDebito.executeUpdate();
            }

            // 4. Crédito no destino
            String sqlCredito = "UPDATE contas SET saldo = saldo + ? WHERE numero = ?";
            try (PreparedStatement stmtCredito = conn.prepareStatement(sqlCredito)) {
                stmtCredito.setDouble(1, valor);
                stmtCredito.setInt(2, numeroDestino);
                stmtCredito.executeUpdate();
            }

            // 5. Se chegou aqui sem erros, confirma a transação!
            conn.commit();
            System.out.println("✅ Transferência de R$ " + valor + " realizada com sucesso!");

        } catch (Exception e) {
            // 6. Se QUALQUER coisa der errado, desfaz TUDO (Rollback)
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("🔄 Transação desfeita (Rollback): " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e; // Repassa a exceção para a camada de serviço tratar
        } finally {
            // 7. Restaura o padrão e fecha a conexão
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Método auxiliar para buscar saldo dentro da mesma conexão da transação
    private ContaCorrente buscarPorNumeroComConn(Connection conn, int numero) throws SQLException {
        String sql = "SELECT numero, titular, saldo FROM contas WHERE numero = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numero);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ContaCorrente(rs.getInt("numero"), rs.getString("titular"), rs.getDouble("saldo"));
            }
        }
        return null;
    }
}