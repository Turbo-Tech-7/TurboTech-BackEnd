package com.garagem52.adapter.output.persistence.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document dentro de OrcamentoEntity.
 * Era uma @Entity separada com @ManyToOne para Orcamento e Peca.
 * No MongoDB vira array embutido — sem collection própria.
 */
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ItemOrcadoEntity {
    @Field("peca_id")
    private String pecaId;      // referência por ObjectId

    @Field("nome_peca")
    private String nomePeca;    // desnormalizado para evitar lookup

    private Double valor;
    private Integer quantidade;
    private String fornecedor;
}
