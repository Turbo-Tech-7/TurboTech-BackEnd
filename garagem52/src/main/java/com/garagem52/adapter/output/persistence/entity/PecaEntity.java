package com.garagem52.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pecas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PecaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peca_id")
    private Long id;

    @Column(nullable = false)
    private String nomePeca;

    @Column(nullable = false)
    private String descricaoPeca;

    @Column(nullable = false)
    private Double precoPeca;
}