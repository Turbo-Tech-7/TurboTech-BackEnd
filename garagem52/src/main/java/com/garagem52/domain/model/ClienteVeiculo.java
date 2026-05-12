package com.garagem52.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Representa a entrada de um cliente na oficina.
 * Criado pelo mecânico ao receber o veículo — associa o cliente ao veículo
 * e serve como ponto de partida para a criação de um orçamento.
 *
 * Salvo na collection "cliente_veiculo" — separada de "users" (mecânicos/admins).
 * O veiculoId referencia a collection "veiculo" (por ObjectId).
 */
@Data
@Builder
public class ClienteVeiculo {
    private String id;             // ObjectId do MongoDB
    private String nomeCliente;
    private String telefoneCliente;
    private String emailCliente;
    private String modeloVeiculo;
    private String placaVeiculo;
    private String veiculoId;
}
