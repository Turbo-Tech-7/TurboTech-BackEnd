package com.garagem52.domain.exception.fornecedor;

public class FornecedorNotFoundException extends RuntimeException {
    public FornecedorNotFoundException(String id) {
        super("Fornecedor não encontrado com id: " + id);
    }
}
