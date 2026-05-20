package com.garagem52.adapter.output.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Collection "cliente_veiculo".
 * Salva os dados do cliente junto com a placa/modelo do veículo.
 * veiculoId referencia a collection "veiculo" (ObjectId) quando o veículo
 * já está cadastrado no sistema; pode ser null se for uma entrada manual.
 */
@Document(collection = "cliente_veiculo")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ClienteVeiculoEntity {

    @Id
    private String id;

    @Field("nome_cliente")
    private String nomeCliente;

    @Field("telefone_cliente")
    private String telefoneCliente;

    @Field("email_cliente")
    private String emailCliente;

    @Field("modelo_veiculo")
    private String modeloVeiculo;

    @Indexed
    @Field("placa_veiculo")
    private String placaVeiculo;

    // Referência ao VeiculoEntity — preenchido após lookup pela placa
    @Field("veiculo_id")
    private String veiculoId;
}
