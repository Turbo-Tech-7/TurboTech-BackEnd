package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Fornecedor {
    private Long id;
    private String nome;
    private String cep;
    private String telefone;
}
