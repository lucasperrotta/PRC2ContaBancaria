/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import exception.SaldoInsuficienteException;

/**
 *
 * @author lucasperrottabarbosa
 */
public abstract class Conta {
    protected int numero;
    protected String titular;
    protected double saldo;
    
    public abstract void sacar(double Valor) throws SaldoInsuficienteException;
    
    public void depositar(double valor) {
        if (valor < 0) {
            this.saldo += saldo;
        } else {
            IO.println("Valor para depósito deve ser maior do que 0");
        }
    }
}
