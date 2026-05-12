package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Item do orçamento informado pelo mecânico.
 * O campo nomePeca substitui pecaId — o mecânico digita o nome da peça
 * sem precisar buscar um ID interno.
 * pecaId continua opcional para quando a peça já estiver cadastrada.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemOrcadoRequestDTO {

    /** Opcional — preenchido automaticamente se nomePeca bater com uma peça cadastrada */
    private String pecaId;

    @NotBlank(message = "Nome da peça/material é obrigatório")
    private String nomePeca;

    private String fornecedor;

    @NotNull(message = "Valor é obrigatório")
    private Double valor;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade mínima é 1")
    private Integer quantidade;
}
