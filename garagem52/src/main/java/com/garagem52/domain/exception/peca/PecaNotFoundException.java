package com.garagem52.domain.exception.peca;

public class PecaNotFoundException extends RuntimeException {

    public PecaNotFoundException(String mensagem) {
        super(mensagem);
    }
}