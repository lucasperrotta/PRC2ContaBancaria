package app;

import dao.ContaDAO;
import model.ContaCorrente;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ContaDAO dao = new ContaDAO();

        try {
            System.out.println("=== TESTE AULA 06 - TRANSAÇÕES ===\n");

            // 1. Listar estado inicial
            System.out.println("--- Estado Inicial ---");
            dao.listarTodas().forEach(c -> System.out.println(c.getTitular() + ": R$ " + c.getSaldo()));

            // 2. Transferência com SUCESSO (Lucas tem 5000, vai enviar 1000 para Maria)
            System.out.println("\n--- Tentando transferência válida (R$ 1000) ---");
            dao.transferir(1001, 1002, 1000.00);

            // 3. Transferência com FALHA (Rollback) - Lucas tenta enviar 99999 (não tem saldo)
            System.out.println("\n--- Tentando transferência INVÁLIDA (R$ 99999) ---");
            try {
                dao.transferir(1001, 1002, 99999.00);
            } catch (Exception e) {
                System.out.println("Erro esperado: " + e.getMessage());
            }

            // 4. Listar estado final para provar que o Rollback funcionou
            System.out.println("\n--- Estado Final (Prova do Rollback) ---");
            List<ContaCorrente> contasFinais = dao.listarTodas();
            for (ContaCorrente c : contasFinais) {
                System.out.println(c.getTitular() + ": R$ " + c.getSaldo());
            }
            
            System.out.println("\n💡 Dica: O saldo do Lucas NÃO pode ter mudado na segunda tentativa!");

        } catch (Exception e) {
            System.err.println("Erro geral: " + e.getMessage());
        }
    }
}