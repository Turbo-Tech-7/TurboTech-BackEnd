package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemOrcado {
    private String pecaId;      // referência por ObjectId
    private String nomePeca;    // desnormalizado
    private String fornecedor;
    private Double valor;
    private Integer quantidade;
}
