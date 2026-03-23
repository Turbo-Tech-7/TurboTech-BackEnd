package com.garagem52.domain.exception.user;

/**
 * HEXAGONAL — DOMAIN EXCEPTION
 * Exceção de regra de negócio: usuário não encontrado.
 * Pertence ao domínio — não carrega nenhum código HTTP.
 * O ExceptionHandler (adapter/input) é quem traduz isso para HTTP 404.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Usuário com ID " + id + " não encontrado.");
    }

    public UserNotFoundException(String email) {
        super("Usuário com e-mail '" + email + "' não encontrado.");
    }
}
