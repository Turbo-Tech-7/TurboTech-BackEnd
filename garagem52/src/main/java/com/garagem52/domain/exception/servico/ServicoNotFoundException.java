package com.garagem52.domain.exception.servico;

public class ServicoNotFoundException extends RuntimeException {
    public ServicoNotFoundException(String id) {
        super("Serviço não encontrado com id: " + id);
    }
}
