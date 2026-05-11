package com.garagem52.adapter.output.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "peca")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PecaEntity {
    @Id
    private String id;

    @Field("nome")
    private String nomePeca;

    @Field("descricao")
    private String descricaoPeca;

    @Field("valor")
    private Double precoPeca;
}
