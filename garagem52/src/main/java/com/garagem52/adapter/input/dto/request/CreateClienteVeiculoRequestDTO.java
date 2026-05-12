package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Preenchido pelo mecânico na tela "Cadastrar Cliente".
 * Campos espelham exatamente o wireframe: Placa, Nome, Telefone, Modelo, Email.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateClienteVeiculoRequestDTO {

    @NotBlank(message = "Placa do veículo é obrigatória")
    private String placaVeiculo;

    @NotBlank(message = "Nome do cliente é obrigatório")
    private String nomeCliente;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefoneCliente;

    @NotBlank(message = "Modelo do veículo é obrigatório")
    private String modeloVeiculo;

    private String emailCliente; // opcional no wireframe
}
