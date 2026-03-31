package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorRequestDTO {

    private String nome;

    @Size(min = 14, message = "O CNPJ deve ter no mínimo 14 caracteres")
    private String cnpj;

    @Size(min = 11, message = "O telefone deve ter no mínimo 11 caracteres")
    private String telefone;
}