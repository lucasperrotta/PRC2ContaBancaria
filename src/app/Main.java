package app;

import model.ContaCorrente;
import service.ContaService;
import exception.SaldoInsuficienteException;
 import java.io.IOException;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Olá Gerenciador de Contas!");
        

        ContaService cs = new ContaService();

        try {
            ContaCorrente c = cs.lerConta("conta.txt");
            cs.addConta(c);
            try {
                cs.solicitaSaque(c,100000);
            } catch (SaldoInsuficienteException e) {
                IO.println("erro: " + e.getMessage());
            }
            cs.atualizaConta(c, "conta_atualizada.txt");
        } catch (IOException e) {
            IO.println("erro: " + e.getMessage());
        }
        cs.listarContas();
        
    }

}
