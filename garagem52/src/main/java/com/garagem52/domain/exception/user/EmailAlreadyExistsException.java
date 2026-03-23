package com.garagem52.domain.exception.user;

/**
 * HEXAGONAL — DOMAIN EXCEPTION
 * Viola a regra de negócio: cada e-mail deve ser único no sistema.
 * Lançada pelo UserService antes de tentar persistir.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Já existe um usuário cadastrado com o e-mail: " + email);
    }
}
