package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVeiculoRequestDTO {

    private String marca;

    private String modelo;

    @Size(min = 3, message = "A cor deve ter no mínimo 3 caracteres")
    private String cor;

}
