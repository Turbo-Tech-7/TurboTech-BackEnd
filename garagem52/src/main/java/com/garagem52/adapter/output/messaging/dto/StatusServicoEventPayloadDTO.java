package com.garagem52.adapter.output.messaging.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Payload do evento "SERVICO_STATUS_ATUALIZADO" (campo `payload` do EventoMessageDTO).
 * O mensageria lê estas chaves do JSON: servicoId, orcamentoId, nomeCliente,
 * emailCliente, veiculo e status (nome do enum ServicoStatus).
 */
@Data
@Builder
public class StatusServicoEventPayloadDTO {
    private String servicoId;
    private String orcamentoId;
    private String nomeCliente;
    private String emailCliente;
    private String veiculo;
    private String status;
}
