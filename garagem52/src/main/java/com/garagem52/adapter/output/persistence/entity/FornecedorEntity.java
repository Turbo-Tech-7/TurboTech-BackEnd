package com.garagem52.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fornecedor")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 45)
    private String cep;

    @Column(length = 45)
    private String telefone;
}
