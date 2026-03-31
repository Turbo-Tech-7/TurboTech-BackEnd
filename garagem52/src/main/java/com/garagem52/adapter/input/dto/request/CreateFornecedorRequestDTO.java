package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateFornecedorRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String cep;

    private String telefone;
}
