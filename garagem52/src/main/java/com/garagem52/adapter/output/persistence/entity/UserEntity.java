package com.garagem52.adapter.output.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class UserEntity {
    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String telefone;
    private String cep;
    private String senha;
    private RoleEntity regra;

    public enum RoleEntity { USER, ADMIN }
}
