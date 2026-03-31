package com.garagem52.domain.exception.fornecedor;

public class FornecedorNotFoundException extends RuntimeException {
    public FornecedorNotFoundException(String message) {
        super(message);
    }
}
