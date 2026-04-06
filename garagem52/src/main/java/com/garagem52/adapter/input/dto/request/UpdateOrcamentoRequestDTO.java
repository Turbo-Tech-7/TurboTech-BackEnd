package com.garagem52.adapter.input.dto.request;

import com.garagem52.domain.utils.enums.MotivoCancelamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateOrcamentoRequestDTO {
    private Double valorMaoDeObra;
    private String descricaoServico;
    private String nomeCliente;
    private String telefoneCliente;
    private String emailCliente;
    private List<ItemOrcadoRequestDTO> itens;
    private OrcamentoStatus status;
    private MotivoCancelamento motivoCancelamento;
}
