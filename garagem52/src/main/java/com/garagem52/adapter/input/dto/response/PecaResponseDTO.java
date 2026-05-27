package com.garagem52.adapter.input.dto.response;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PecaResponseDTO {
    private String idPeca;
    private String nomePeca;
    private String descricaoPeca;
    private Double precoPeca;
    private String fornecedor;
}
