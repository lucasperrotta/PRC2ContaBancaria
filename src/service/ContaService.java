package service;

import model.ContaCorrente;
import exception.SaldoInsuficienteException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors; // Importante para o groupingBy
import strategy.TarifaStrategy;

/**
 *
 * @author Lucas Perrotta
 */
public class ContaService {

    private final ArrayList<ContaCorrente> contasCorrentes;

    public ContaService() {
        this.contasCorrentes = new ArrayList<>();
    }

    public boolean adicionarNovaConta(int numero, String titular, double saldoInicial) {
        // .isPresent() verifica se o Optional tem um valor (ou seja, se achou a conta)
        if (buscarPorNumero(numero).isPresent()) {
            IO.println("Erro: Já existe uma conta com o número " + numero);
            return false;
        }

        contasCorrentes.add(new ContaCorrente(numero, titular, saldoInicial));
        IO.println("✅ Conta #" + numero + " de " + titular + " adicionada com sucesso!");
        return true;
    }

    public void listarContas() {
        for (ContaCorrente c : contasCorrentes) {
            c.imprimirConta();
        }
    }

    public List<ContaCorrente> lerContas(String caminho) throws IOException {
        Path path = Paths.get(caminho);
        List<String> infoContas = Files.readAllLines(path);

        for (String infoConta : infoContas) {
            String[] splitConta = infoConta.split(",");
            int numero = Integer.parseInt(splitConta[0].trim());
            String titular = splitConta[1].trim();
            double saldoInicial = Double.parseDouble(splitConta[2]);
            contasCorrentes.add(new ContaCorrente(numero, titular, saldoInicial));
        }
        IO.println("Contas carregadas: ");
        listarContas();
        return contasCorrentes;
    }

    public void solicitaSaque(ContaCorrente conta, double valor) throws SaldoInsuficienteException {
        conta.sacar(valor);
    }

    public void solicitaDeposito(ContaCorrente conta, double valor) {
        conta.depositar(valor);
    }

    public void atualizaContas(ContaCorrente conta, String caminho) throws IOException {
        Path path = Paths.get(caminho);
        ArrayList dadosAtualizados = new ArrayList<>();

        for (ContaCorrente contaCorrente : contasCorrentes) {
            dadosAtualizados.add(contaCorrente.getNumero() + ", " + contaCorrente.getTitular() + ", " + contaCorrente.getSaldo());
        }
        Files.write(path, dadosAtualizados);
    }

    public ArrayList<ContaCorrente> getContasCorrentes() {
        return contasCorrentes;
    }

    public Optional<ContaCorrente> buscarPorNumero(int numero) {
        return contasCorrentes.stream()
                .filter(c -> c.getNumero() == numero)
                .findFirst();
    }

    // ✅ REQUISITO 1: Filtrar contas com saldo > 10000
    public List<ContaCorrente> filtrarContasRicas() {
        return contasCorrentes.stream()
                .filter(c -> c.getSaldo() > 10000) // Predicate (Lambda)
                .collect(Collectors.toList());     // Operação Terminal
    }

    // ✅ REQUISITO 2: Calcular saldo total (usando reduce() conforme pedido no PDF)
    public double calcularSaldoTotal() {
        // O mapToDouble extrai o saldo de cada conta.
        // O reduce() soma tudo/*,*/ começando do 0.
        return contasCorrentes.stream()
                .mapToDouble(ContaCorrente::getSaldo)
                .reduce(0, Double::sum);
    }

    // ✅ REQUISITO 3: Agrupar contas por faixa de saldo (groupingBy)
    public Map<String, List<ContaCorrente>> agruparPorFaixaDeSaldo() {
        return contasCorrentes.stream()
                .collect(Collectors.groupingBy(c -> {
                    // Lógica de classificação (A chave do Map será a String retornada aqui)
                    if (c.getSaldo() <= 5000) {
                        return "Até R$ 5.000";
                    } else if (c.getSaldo() <= 10000) {
                        return "De R$ 5.001 a R$ 10.000";
                    } else {
                        return "Acima de R$ 10.000";
                    }
                }));
    }

    /*filter(c -> c.getSaldo() > 10000): É o equivalente ao WHERE saldo > 10000 do SQL. A lambda c -> ... é o Predicate.
reduce(0, Double::sum): O PDF pediu reduce(). Ele funciona como um acumulador. O 0 é o valor inicial, e Double::sum é a operação que ele faz a cada passo (soma o acumulado com o próximo saldo).
Nota: Você poderia usar .sum() direto no mapToDouble, mas como a professora pediu reduce(), usamos essa forma para garantir a nota.
Collectors.groupingBy(...): É o equivalente ao GROUP BY do SQL. A lambda dentro dele define a chave do grupo. O resultado é sempre um Map<Chave, ListaDeObjetos>
/*/
    // ✅ REQUISITO 1: PREDICATE (Filtragem)
    public List<ContaCorrente> filtrarSaldoMaiorQue5000() {
        Predicate<ContaCorrente> saldoAlto = c -> c.getSaldo() > 5000;
        return contasCorrentes.stream()
                .filter(saldoAlto)
                .collect(Collectors.toList());
    }

    public List<ContaCorrente> filtrarNumeroPar() {
        Predicate<ContaCorrente> numeroPar = c -> c.getNumero() % 2 == 0;
        return contasCorrentes.stream()
                .filter(numeroPar)
                .collect(Collectors.toList());
    }

    // ✅ REQUISITO 2: COMPARATOR (Ordenação com Lambda)
    public void ordenarPorSaldoDecrescente() {
        Comparator<ContaCorrente> porSaldo = (c1, c2) -> Double.compare(c2.getSaldo(), c1.getSaldo());
        contasCorrentes.sort(porSaldo);
    }

    public void ordenarPorTitularAlfabetico() {
        Comparator<ContaCorrente> porNome = (c1, c2) -> c1.getTitular().compareToIgnoreCase(c2.getTitular());
        contasCorrentes.sort(porNome);
    }

    // ✅ REQUISITO 3: STRATEGY (Enum de Tarifa)
    public void aplicarTarifa(TarifaStrategy estrategia) {
        for (ContaCorrente c : contasCorrentes) {
            double valorTarifa = estrategia.calcular(c.getSaldo());
            c.setSaldo(c.getSaldo() - valorTarifa);
            System.out.printf("Tarifa de R$ %.2f aplicada na conta de %s.%n", valorTarifa, c.getTitular());
        }
    }
}
