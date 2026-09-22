package strategy;

public enum TarifaStrategy {
    
    FIXA {
        @Override
        public double calcular(double saldo) {
            return 10.00; // R$ 10,00 fixo
        }
    },
    
    PERCENTUAL {
        @Override
        public double calcular(double saldo) {
            return saldo * 0.01; // 1% do saldo
        }
    },
    
    ISENTA {
        @Override
        public double calcular(double saldo) {
            return 0.0; // Sem tarifa
        }
    };

    // Método abstrato que cada constante é OBRIGADA a implementar
    public abstract double calcular(double saldo);
}