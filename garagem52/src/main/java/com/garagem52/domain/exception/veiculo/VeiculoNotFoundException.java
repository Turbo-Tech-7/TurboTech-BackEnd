package com.garagem52.domain.exception.veiculo;

public class VeiculoNotFoundException extends  RuntimeException{

    public VeiculoNotFoundException(String id){
        super("Veículo com id " + id + " não encontrado");
    }

}
