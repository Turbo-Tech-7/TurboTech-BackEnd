package com.garagem52.adapter.input.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ServicoResponseDTO {
    private Long id;
    private String servicoOrcado;
    private Long veiculoId;
    private LocalDateTime dataEntrada;
    private String descricaoProblema;
    private String status;
}
