package com.garagem52.adapter.input.dto.response;

import com.garagem52.domain.utils.enums.MotivoCancelamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class OrcamentoResponseDTO {
    private String id;
    private String servicoId;
    private String veiculoId;
    private String clienteVeiculoId;
    private Double valorMaoDeObra;
    private Double valorTotal;
    private LocalDateTime dataOrcamento;
    private OrcamentoStatus status;
    private String statusDescricao;
    private MotivoCancelamento motivoCancelamento;
    private String motivoCancelamentoDescricao;
    private List<ItemOrcadoResponseDTO> itens;
    private String descricaoServico;

    // Dados do cliente — preenchidos do ClienteVeiculo quando disponível
    private String nomeCliente;
    private String telefoneCliente;
    private String emailCliente;

    /** Dados completos do veículo (enriquecido) */
    private VeiculoResponseDTO veiculo;

    /** Dados completos do cliente+veículo (enriquecido) */
    private ClienteVeiculoResponseDTO clienteVeiculo;
}
