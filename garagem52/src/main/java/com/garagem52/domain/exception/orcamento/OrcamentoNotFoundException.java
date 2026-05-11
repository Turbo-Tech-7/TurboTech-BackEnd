package com.garagem52.domain.exception.orcamento;

public class OrcamentoNotFoundException extends RuntimeException {
    public OrcamentoNotFoundException(String id) {
        super("Orçamento não encontrado com id: " + id);
    }
}
