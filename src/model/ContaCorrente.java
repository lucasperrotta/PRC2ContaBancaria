package model;

import exception.SaldoInsuficienteException;

public class ContaCorrente extends Conta {

    public ContaCorrente(int numero, String titular, double valor) {
        super(numero, titular, valor);
    }

    /**
     *
     * @param valor
     * @throws SaldoInsuficienteException
     */
    @Override
    public void sacar(double valor) throws SaldoInsuficienteException {
        if (valor <= 0) {
            throw new SaldoInsuficienteException(this.titular + " não da pra sacar 0 nem menos né e.e'");
        } else if (valor > this.saldo) {
            throw new SaldoInsuficienteException(titular + " sepá ce não tem tudo isso na conta ein ^^'");
        } else {
            this.saldo -= valor;
        }
    }
}
