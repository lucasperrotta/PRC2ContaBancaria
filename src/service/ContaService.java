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

    public ContaCorrente lerContas(String caminho) throws IOException {
        Path path = Paths.get(caminho);
        List<String> linhas = Files.readAllLines(path);
        String[] infosConta = linhas.get(0).split(",");
        int numero = Integer.parseInt(infosConta[0].trim());
        String titular = infosConta[1].trim();
        double saldoInicial = Double.parseDouble(infosConta[2]);
        return new ContaCorrente(numero, titular, saldoInicial);
    }

    public void solicitaSaque(ContaCorrente conta, double valor) throws SaldoInsuficienteException {
        conta.sacar(valor);
    }
    
    public void atualizaConta (ContaCorrente conta, String caminho) throws IOException {
        Path path = Paths.get(caminho);
        String dadosAtualizados = conta.getNumero()+", "+conta.getTitular()+", "+conta.getSaldo();
        Files.write(path, dadosAtualizados.getBytes());
    
    }
}
