package com.garagem52.adapter.input.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FornecedorResponseDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private String telefone;
}