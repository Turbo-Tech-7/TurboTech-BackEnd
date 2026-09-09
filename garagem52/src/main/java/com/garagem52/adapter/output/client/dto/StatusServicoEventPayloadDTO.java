package com.garagem52.adapter.output.client.dto;

import lombok.Builder;
import lombok.Data;

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
