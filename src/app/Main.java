package app;

import model.ContaCorrente;
import service.ContaService;
import exception.SaldoInsuficienteException;
import java.io.IOException;
import java.util.List;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Olá Gerenciador de Contas!");
        

        ContaService cs = new ContaService();
        
        try {
            List<ContaCorrente> contas = cs.lerContas("conta.txt");
            int contaOperacao = Integer.parseInt(IO.readln("Número da conta para operar: "));
            
            
            
            ContaCorrente c = contas.get(contaOperacao);
            IO.println("Conta selecionada:");
            c.imprimirConta();
            int operacao = Integer.parseInt(IO.readln("1 para saque 2 para dep: "));
            switch (operacao) {
                case 1:
                    double valorSaque = Double.parseDouble(IO.readln("Valor para saque: R$"));
                    try {
                cs.solicitaSaque(c,valorSaque);
            } catch (SaldoInsuficienteException e) {
                IO.println("erro: " + e.getMessage());
            }
                    break;
                default:
                    throw new AssertionError();
            }
            cs.addConta(c);
            
            cs.atualizaContas(c, "conta_atualizada.txt");
        } catch (IOException e) {
              IO.println("Erro de arquivo: Verifique se 'conta.txt' existe na raiz do projeto.\n"+e.getMessage());
        } catch (NumberFormatException e) {
            IO.println("Erro: Por favor, digite um valor numérico válido (ex: 500.00).\n"+e.getMessage());
        }
        cs.listarContas();
        
    }

}
