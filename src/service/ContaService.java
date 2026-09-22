/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.ContaCorrente;
import exception.SaldoInsuficienteException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;

/**
 *
 * @author Lucas Perrotta
 */
public class ContaService {

    private List<ContaCorrente> contasCorrentes = new ArrayList<>();

    public void addConta(ContaCorrente conta) {
        contasCorrentes.add(conta);
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
    
    public void atualizaContas(ContaCorrente conta, String caminho) throws IOException {
        Path path = Paths.get(caminho);
        String dadosAtualizados = conta.getNumero() + ", " + conta.getTitular() + ", " + conta.getSaldo();
        Files.writeString(path, dadosAtualizados);
    }
    
    public List<ContaCorrente> getContasCorrentes(){
        return contasCorrentes;
    }
}
