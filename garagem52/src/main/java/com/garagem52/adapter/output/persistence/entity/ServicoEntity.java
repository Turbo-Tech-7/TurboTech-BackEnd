package com.garagem52.adapter.output.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "servico")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ServicoEntity {
    @Id
    private String id;

    @Field("servico_orcado")
    private String servicoOrcado;

    // Era @ManyToOne VeiculoEntity — agora referência por ObjectId
    @Indexed
    @Field("veiculo_id")
    private String veiculoId;

    @Field("data_entrada")
    private LocalDateTime dataEntrada;

    @Field("descricao_problema")
    private String descricaoProblema;

    private String status;
}
