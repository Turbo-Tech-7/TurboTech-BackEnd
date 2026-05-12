package com.garagem52.adapter.input.dto.request;

import lombok.*;

/**
 * Usado apenas internamente — o mecânico não cria Serviço diretamente.
 * O Serviço é criado automaticamente pelo OrcamentoService ao criar um orçamento,
 * derivando o veiculoId do ClienteVeiculo.
 *
 * Este DTO serve para criação manual via endpoint /servicos quando necessário
 * (ex: pelo admin para registrar uma OS avulsa).
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateServicoRequestDTO {

    private String servicoOrcado;

    /** ID do veículo — necessário apenas na criação manual via /servicos */
    private String veiculoId;

    private String descricaoProblema;
}
