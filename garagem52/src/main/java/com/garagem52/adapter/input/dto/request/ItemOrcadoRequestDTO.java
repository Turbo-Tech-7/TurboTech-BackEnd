package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemOrcadoRequestDTO {

    @NotNull(message = "ID da peça é obrigatório")
    private Long pecaId;

    private String fornecedor;

    @NotNull(message = "Valor é obrigatório")
    private Double valor;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade mínima é 1")
    private Integer quantidade;
}
