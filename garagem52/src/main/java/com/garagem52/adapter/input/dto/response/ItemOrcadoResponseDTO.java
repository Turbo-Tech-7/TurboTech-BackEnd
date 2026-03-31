package com.garagem52.adapter.input.dto.response;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ItemOrcadoResponseDTO {
    private Long id;
    private Long pecaId;
    private String nomePeca;
    private String fornecedor;
    private Double valor;
    private Integer quantidade;
}
