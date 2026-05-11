package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String id;
    private String name;
    private String email;
    private String telefone;
    private String cep;
    private String senha;
    private Role regra;

    public enum Role {
        USER, ADMIN
    }
}
