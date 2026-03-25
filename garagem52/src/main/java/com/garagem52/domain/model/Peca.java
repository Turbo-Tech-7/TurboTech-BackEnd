package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Peca {

    private Long id;
    private String nome;
    private String descricao;
    private Double valor;
}