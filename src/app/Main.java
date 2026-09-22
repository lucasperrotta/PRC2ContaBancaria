package app;

import model.ContaCorrente;
import service.ContaService;
import exception.SaldoInsuficienteException;

public class Main {
    public static void main(String[] args) {
        IO.println("=== TESTE AULA 05 - JDBC ===");
        ContaService cs = new ContaService();

        // 1. Testar Listagem (Já carrega no construtor)
        cs.listarContas();

        // 2. Testar Inserção
        IO.println("\n--- Inserindo nova conta ---");
        cs.addConta(new ContaCorrente(1004, "Ana Souza", 5000.00));

        // 3. Testar Depósito e Saque (Atualiza o BD)
        IO.println("\n--- Operações ---");
        ContaCorrente contaLucas = cs.buscarConta(1001);
        if (contaLucas != null) {
            cs.solicitaDeposito(contaLucas, 1000.00);
            
            try {
                cs.solicitaSaque(contaLucas, 500.00);
            } catch (SaldoInsuficienteException e) {
                IO.println("Erro: " + e.getMessage());
            }
        }

        // 4. Testar Remoção
        IO.println("\n--- Removendo conta 1004 ---");
        cs.removerConta(1004);

        // 5. Listagem Final
        IO.println("\n--- Estado Final ---");
        cs.listarContas();
        
        IO.println("\n✅ Dica: Dê um SELECT no seu MySQL para confirmar que tudo foi salvo!");
    }
}