package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemOrcado {
    private Long id;
    private Long orcamentoId;
    private Long pecaId;
    private String nomePeca;
    private String fornecedor;
    private Double valor;
    private Integer quantidade;
}
