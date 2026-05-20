package com.garagem52.adapter.output.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fornecedor")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class FornecedorEntity {
    @Id
    private String id;

    private String nome;
    private String cep;
    private String telefone;
}
