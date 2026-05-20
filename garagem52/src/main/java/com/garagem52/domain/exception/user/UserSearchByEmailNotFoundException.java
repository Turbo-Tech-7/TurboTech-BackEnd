package com.garagem52.domain.exception.user;

public class UserSearchByEmailNotFoundException extends RuntimeException {
    public UserSearchByEmailNotFoundException(String email) {
        super("Usuário com e-mail '" + email + "' não encontrado.");
    }
}
