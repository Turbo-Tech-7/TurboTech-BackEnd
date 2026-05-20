package com.garagem52.domain.model;

import com.garagem52.domain.utils.enums.MotivoCancelamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Orcamento {
    private String id;
    private String servicoId;
    private String veiculoId;

    /** Referência ao ClienteVeiculo — preferencial para dados do cliente */
    private String clienteVeiculoId;

    private Double valorMaoDeObra;
    private Double valorTotal;
    private LocalDateTime dataOrcamento;
    private OrcamentoStatus status;
    private MotivoCancelamento motivoCancelamento;

    /** Enriquecido no adapter via lookup */
    private Veiculo veiculo;

    /** Enriquecido no adapter via lookup ao ClienteVeiculo */
    private ClienteVeiculo clienteVeiculo;

    private List<ItemOrcado> itens;
    private String descricaoServico;

    // Fallback / compatibilidade
    private String nomeCliente;
    private String telefoneCliente;
    private String emailCliente;
}
