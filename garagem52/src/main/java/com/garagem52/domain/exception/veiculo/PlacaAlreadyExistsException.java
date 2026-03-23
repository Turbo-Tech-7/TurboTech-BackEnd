package com.garagem52.domain.exception.veiculo;

public class PlacaAlreadyExistsException extends RuntimeException {

    public PlacaAlreadyExistsException(String placa){
        super("Já existe um veículo com essa placa: " + placa);
    }

}
