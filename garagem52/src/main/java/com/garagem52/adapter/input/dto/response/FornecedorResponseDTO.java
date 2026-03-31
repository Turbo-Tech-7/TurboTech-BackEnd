package com.garagem52.adapter.input.dto.response;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class FornecedorResponseDTO {
    private Long id;
    private String nome;
    private String cep;
    private String telefone;
}
