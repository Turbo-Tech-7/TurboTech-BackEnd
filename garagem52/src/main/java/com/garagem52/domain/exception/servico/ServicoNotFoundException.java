package com.garagem52.domain.exception.servico;

public class ServicoNotFoundException extends RuntimeException {
    public ServicoNotFoundException(Long id) {
        super("Serviço não encontrado com id: " + id);
    }
}
