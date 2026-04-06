package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateServicoRequestDTO {

    private String servicoOrcado;

    @NotNull(message = "ID do veículo é obrigatório")
    private Long veiculoId;

    private String descricaoProblema;
}
