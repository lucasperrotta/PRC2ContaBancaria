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
    protected int    numero ;
    protected String titular;
    protected double saldo  ;
    
    public abstract void sacar(double Valor) throws SaldoInsuficienteException;
    
    public void depositar(double valor) {
        this.saldo += saldo;
    }
    
    public void imprimirConta() {
        IO.println("Nome: "+this.titular) ;
        IO.println("Nº: "+this.numero)    ;
        IO.println("Saldo: "+this.saldo)  ;
    }
}
