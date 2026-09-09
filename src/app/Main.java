package app;
import model.ContaCorrente;
import exception.SaldoInsuficienteException;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Olá Gerenciador de Contas!");
        ContaCorrente c = new ContaCorrente(0, "Lucas", 8000);
        
        try {
            c.sacar(100000);
        } catch(SaldoInsuficienteException e) {
            IO.println("erro: "+e.getMessage());
        }
    }
    
}
