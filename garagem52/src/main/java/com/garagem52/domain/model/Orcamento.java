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
    private Long id;
    private Long servicoId;
    private Long veiculoId;
    private Double valorMaoDeObra;
    private Double valorTotal;
    private LocalDateTime dataOrcamento;
    private OrcamentoStatus status;
    private MotivoCancelamento motivoCancelamento;
    private Veiculo veiculo;
    private List<ItemOrcado> itens;
    private String descricaoServico;
    private String nomeCliente;
    private String telefoneCliente;
    private String emailCliente;
}
