package app;

import model.ContaCorrente;
import service.ContaService;
import exception.SaldoInsuficienteException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import strategy.TarifaStrategy;

public class Main {

    public static void main(String[] args) {
        ContaService cs = new ContaService();
        boolean rodando = true;

        try {
            // Carrega as contas do arquivo na inicialização
            cs.lerContas("contas.txt");
            // ... dentro do main, após carregar as contas ...

            IO.println("\n=== DESAFIO STREAMS (AULA 03) ===");

// 1. Testando o Filtro
            IO.println("\n1. Contas com saldo > R$ 10.000:");
            List<ContaCorrente> contasRicas = cs.filtrarContasRicas();
            if (contasRicas.isEmpty()) {
                IO.println("Nenhuma conta encontrada com esse saldo.");
            } else {
                contasRicas.forEach(ContaCorrente::imprimirConta); // Method Reference
            }

// 2. Testando o Reduce (Saldo Total)
            IO.println("\n2. Saldo Total do Banco:");
            double total = cs.calcularSaldoTotal();
            IO.println("Total em caixa: R$ " + total);

// 3. Testando o GroupingBy (Agrupamento)
            IO.println("\n3. Contas Agrupadas por Faixa de Saldo:");
            Map<String, List<ContaCorrente>> faixas = cs.agruparPorFaixaDeSaldo();

// Iterando sobre o Map (Chave = Faixa, Valor = Lista de Contas)
            faixas.forEach((faixa, listaDeContas) -> {
                IO.println("\n--- Faixa: " + faixa + " ---");
                listaDeContas.forEach(c -> IO.println("   * " + c.getTitular() + " (R$ " + c.getSaldo() + ")"));
            });
            IO.println("\n=== AULA 04 - STRATEGY, PREDICATE E COMPARATOR ===");
            
            // 1. Testando PREDICATE
            IO.println("\n--- Contas com saldo > R$ 5.000 ---");
            cs.filtrarSaldoMaiorQue5000().forEach(ContaCorrente::imprimirConta);
            
            IO.println("\n--- Contas com número par ---");
            cs.filtrarNumeroPar().forEach(ContaCorrente::imprimirConta);

            // 2. Testando COMPARATOR
            IO.println("\n--- Ordenando por saldo decrescente ---");
            cs.ordenarPorSaldoDecrescente();
            cs.listarContas();
            
            IO.println("\n--- Ordenando por nome alfabético ---");
            cs.ordenarPorTitularAlfabetico();
            cs.listarContas();

            // 3. Testando STRATEGY (Enum)
            IO.println("\n--- Aplicando tarifa PERCENTUAL (1%) ---");
            cs.aplicarTarifa(TarifaStrategy.PERCENTUAL);
            cs.listarContas();
            
            // Salva o estado final
            cs.atualizaContas(cs.getContasCorrentes().get(0), "contas_atualizadas.txt");

            while (rodando) {
                IO.println("\n=== GERENCIADOR DE CONTAS BANCÁRIAS ===");
                IO.println("Contas cadastradas:");
                cs.listarContas();

                IO.println("\n[1] Operar em uma conta existente (Saque/Depósito)");
                IO.println("[2] Adicionar nova conta");
                IO.println("[0] Sair e Salvar");
                String opcaoMenu = IO.readln("Escolha uma opção: ");

                switch (opcaoMenu) {
                    case "1" -> {
                        // Usuário digita o NÚMERO da conta, não o índice
                        int numConta = Integer.parseInt(IO.readln("Digite o número da conta: "));
                        ContaCorrente contaAlvo = cs.buscarPorNumero(numConta)
                                .orElseThrow(() -> new RuntimeException("Conta não encontrada!"));

                        if (contaAlvo == null) {
                            IO.println("❌ Conta não encontrada!");
                        } else {
                            IO.println("\nConta selecionada: " + contaAlvo.getTitular() + " (Saldo: R$ " + contaAlvo.getSaldo() + ")");
                            IO.println("[1] Sacar");
                            IO.println("[2] Depositar");
                            String opOperacao = IO.readln("Operação: ");

                            double valor = Double.parseDouble(IO.readln("Valor: R$ "));

                            if (opOperacao.equals("1")) {
                                try {
                                    cs.solicitaSaque(contaAlvo, valor);
                                    IO.println("✅ Saque realizado!");
                                } catch (SaldoInsuficienteException e) {
                                    IO.println(" " + e.getMessage());
                                }
                            } else if (opOperacao.equals("2")) {
                                cs.solicitaDeposito(contaAlvo, valor);
                                IO.println("✅ Depósito realizado!");
                            }
                        }
                    }

                    case "2" -> {
                        int novoNum = Integer.parseInt(IO.readln("Número da nova conta: "));
                        String novoTitular = IO.readln("Titular: ");
                        double novoSaldo = Double.parseDouble(IO.readln("Saldo inicial: R$ "));
                        cs.adicionarNovaConta(novoNum, novoTitular, novoSaldo);
                    }

                    case "0" -> {
                        cs.atualizaContas(cs.getContasCorrentes().get(0), "contas_atualizadas.txt"); // Salva tudo
                        IO.println("💾 Dados salvos. Até logo!");
                        rodando = false;
                    }

                    default ->
                        IO.println("Opção inválida!");
                }
            }
        } catch (IOException e) {
            IO.println("Erro de arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            IO.println("Erro: Digite apenas números válidos.");
        }
    }
}
