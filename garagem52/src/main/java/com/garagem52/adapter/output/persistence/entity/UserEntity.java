package com.garagem52.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * ADAPTER/OUTPUT — ORM ENTITY
 * Representação da tabela "users" no banco MySQL para o Hibernate.
 * ORM (Object-Relational Mapping) com JPA/Hibernate:
 *   Entity  → esta classe é gerenciada pelo Hibernate
 *   Table   → define o nome da tabela no banco
 *   Column  → define constraints da coluna (unique, nullable)
 *   Id + GeneratedValue → chave primária auto-incrementada (IDENTITY do MySQL)
 * Esta classe NÃO é o modelo de domínio — é a representação de persistência.
 * A separação impede que o domínio dependa do JPA (framework de infraestrutura).
 * O UserPersistenceMapper faz a conversão entre as duas.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefone;

    private String cep;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEntity regra;

    /** Enum de persistência — espelho do User.Role do domínio */
    public enum RoleEntity {
        USER, ADMIN
    }
}
