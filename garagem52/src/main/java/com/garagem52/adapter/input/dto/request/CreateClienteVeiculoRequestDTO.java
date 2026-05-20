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

    // Preenchido automaticamente pelo retorno da API de veículos (marca + modelo)
    // Não é obrigatório no request — serve apenas como fallback se a API não retornar dados
    private String modeloVeiculo;

    private String emailCliente; // opcional no wireframe
}
