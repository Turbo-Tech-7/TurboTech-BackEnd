package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Peca {
    private String id;  // ObjectId do MongoDB
    private String nome;
    private String descricao;
    private Double valor;
    private String fornecedor;
    private String unidadeVenda;
}