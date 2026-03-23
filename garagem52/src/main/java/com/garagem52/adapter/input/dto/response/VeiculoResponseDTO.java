package com.garagem52.adapter.input.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoResponseDTO {

    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String placa;
    private String cor;

}
