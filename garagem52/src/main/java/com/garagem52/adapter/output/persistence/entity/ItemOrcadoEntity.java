package com.garagem52.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_orcado")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ItemOrcadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private OrcamentoEntity orcamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peca_id", nullable = false)
    private PecaEntity peca;

    @Column
    private Double valor;

    private Integer quantidade;

    @Column
    private String fornecedor;
}
