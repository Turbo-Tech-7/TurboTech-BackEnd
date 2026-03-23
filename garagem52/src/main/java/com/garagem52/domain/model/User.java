package com.garagem52.domain.model;

import lombok.*;

/**
 * HEXAGONAL — DOMAIN MODEL
 * Entidade de domínio pura: representa o conceito de "Usuário" no núcleo do hexágono.
 * Sem nenhuma anotação de framework (@Entity, @JsonProperty, etc.).
 * Completamente independente — pode ser testada sem banco, sem HTTP, sem nada externo.
 */
@Data
@Builder
public class User {

    private Long id;
    private String name;
    private String email;
    private String telefone;
    private String cep;

    /**
     * Armazenada sempre como hash bcrypt.
     * O domínio não faz o hash — essa responsabilidade é do UserService (application).
     */
    private String senha;

    private Role regra;

    public enum Role {
        USER, ADMIN
    }
}
