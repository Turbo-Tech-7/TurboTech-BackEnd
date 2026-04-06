package com.garagem52.adapter.input.dto.request;

import com.garagem52.domain.utils.enums.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelarOrcamentoRequestDTO {

    @NotNull(message = "O motivo do cancelamento é obrigatório")
    private MotivoCancelamento motivo;
}
