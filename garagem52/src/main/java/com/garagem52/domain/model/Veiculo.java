package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Veiculo {

    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String placa;
    private String cor;

}
