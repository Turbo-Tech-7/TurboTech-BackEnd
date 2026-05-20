package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemOrcado {
    private String pecaId;    // opcional — preenchido se a peça estiver cadastrada
    private String nomePeca;  // obrigatório — nome informado pelo mecânico
    private String fornecedor;
    private Double valor;
    private Integer quantidade;
}
