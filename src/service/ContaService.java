package service;

import dao.ContaDAO;
import model.ContaCorrente;
import exception.SaldoInsuficienteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContaService {
    private List<ContaCorrente> contasCorrentes = new ArrayList<>();
    private ContaDAO dao;

    public ContaService() {
        this.dao = new ContaDAO();
        carregarDoBanco(); // Carrega do BD ao iniciar
    }

    private void carregarDoBanco() {
        try {
            contasCorrentes = dao.listar();
            IO.println("✅ " + contasCorrentes.size() + " contas carregadas do Banco de Dados.");
        } catch (SQLException e) {
            IO.println("❌ Erro ao conectar com o banco: " + e.getMessage());
        }
    }

    public void addConta(ContaCorrente conta) {
        try {
            dao.inserir(conta); // Salva no BD
            contasCorrentes.add(conta); // Adiciona na memória
            IO.println("✅ Conta salva no banco e na memória.");
        } catch (SQLException e) {
            IO.println("❌ Erro ao salvar conta: " + e.getMessage());
        }
    }

    public void solicitaSaque(ContaCorrente conta, double valor) throws SaldoInsuficienteException {
        conta.sacar(valor);
        try {
            // Atualiza o saldo diretamente no banco após o saque
            dao.atualizarSaldo(conta.getNumero(), conta.getSaldo());
            IO.println(" Saldo atualizado no banco.");
        } catch (SQLException e) {
            IO.println("❌ Erro ao atualizar banco: " + e.getMessage());
        }
    }

    public void solicitaDeposito(ContaCorrente conta, double valor) {
        conta.depositar(valor);
        try {
            dao.atualizarSaldo(conta.getNumero(), conta.getSaldo());
        } catch (SQLException e) {
            IO.println("❌ Erro ao atualizar banco: " + e.getMessage());
        }
    }

    public void removerConta(int numero) {
        try {
            dao.remover(numero);
            contasCorrentes.removeIf(c -> c.getNumero() == numero);
            IO.println("🗑️ Conta removida do banco.");
        } catch (SQLException e) {
            IO.println("❌ Erro ao remover: " + e.getMessage());
        }
    }

    public void listarContas() {
        IO.println("\n--- Contas em Memória (Sincronizadas com BD) ---");
        for (ContaCorrente c : contasCorrentes) {
            c.imprimirConta();
        }
    }
    
    // Getter para o Main poder buscar contas
    public ContaCorrente buscarConta(int numero) {
        return contasCorrentes.stream()
                .filter(c -> c.getNumero() == numero)
                .findFirst().orElse(null);
    }
}