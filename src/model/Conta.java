package model;
import exception.SaldoInsuficienteException;

public abstract class Conta {
    protected int    numero ;
    protected String titular;
    protected double saldo  ;
    
    public abstract void sacar(double Valor) throws SaldoInsuficienteException;
    
    public void depositar(double valor) {
        if (valor < 0) {
            this.saldo += saldo;
        } else {
            IO.println("Valor para depósito deve ser maior do que 0");
        }
    }
    
    public void imprimirConta() {
        IO.println("Nome: "+this.titular);
        IO.println("Nº: "+this.numero)   ;
        IO.println("Saldo: "+this.saldo) ;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Conta(int numero, String titular, double saldo) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldo;
    }
}
