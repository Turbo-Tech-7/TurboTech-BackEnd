package com.garagem52.adapter.output.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "veiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeiculoEntity {
    @Id
    private String id;

    private String marca;
    private String modelo;
    private Integer ano;

    @Indexed(unique = true)
    private String placa;

    private String cor;
}
