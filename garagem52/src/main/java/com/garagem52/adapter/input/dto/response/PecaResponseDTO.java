package com.garagem52.adapter.input.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PecaResponseDTO {

    private Long idPeca;
    private String nomePeca;
    private String descricaoPeca;
    private Double precoPeca;
}