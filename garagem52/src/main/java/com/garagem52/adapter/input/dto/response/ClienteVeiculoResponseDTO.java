package com.garagem52.adapter.input.dto.response;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ClienteVeiculoResponseDTO {
    private String id;
    private String nomeCliente;
    private String telefoneCliente;
    private String emailCliente;
    private String placaVeiculo;
    private String modeloVeiculo;
    /** Preenchido automaticamente se o veículo já existir na collection veiculo */
    private String veiculoId;
    /** Dados completos do veículo quando encontrado */
    private VeiculoResponseDTO veiculo;
}
