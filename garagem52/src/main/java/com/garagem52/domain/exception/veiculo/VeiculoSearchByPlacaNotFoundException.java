package com.garagem52.domain.exception.veiculo;

public class VeiculoSearchByPlacaNotFoundException extends RuntimeException {
    public VeiculoSearchByPlacaNotFoundException(String placa) {
        super("Veículo com a placa " + placa + " não encontrado");
    }
}
