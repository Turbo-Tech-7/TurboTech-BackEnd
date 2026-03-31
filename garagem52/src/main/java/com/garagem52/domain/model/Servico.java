package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Servico {
    private Long id;
    private String servicoOrcado;
    private Long veiculoId;
    private LocalDateTime dataEntrada;
    private String descricaoProblema;
    private String status;
}
