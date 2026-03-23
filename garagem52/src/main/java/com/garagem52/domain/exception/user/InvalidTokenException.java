package com.garagem52.domain.exception.user;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String token) {
        super("Token digitado é inválido: " + token);
    }

}
