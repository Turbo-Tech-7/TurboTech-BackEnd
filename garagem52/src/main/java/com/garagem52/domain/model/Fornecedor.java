package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Fornecedor {
    private String id;  // ObjectId do MongoDB
    private String nome;
    private String cep;
    private String telefone;
}
