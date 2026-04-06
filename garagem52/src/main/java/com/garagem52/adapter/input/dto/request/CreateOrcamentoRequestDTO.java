package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrcamentoRequestDTO {

    @NotNull(message = "ID do serviço é obrigatório")
    private Long servicoId;

    @NotNull(message = "ID do veículo é obrigatório")
    private Long veiculoId;

    @NotBlank(message = "Descrição do serviço é obrigatória")
    private String descricaoServico;

    private String nomeCliente;

    private String telefoneCliente;

    private String emailCliente;

    @NotNull(message = "Valor da mão de obra é obrigatório")
    private Double valorMaoDeObra;

    @NotEmpty(message = "Ao menos um item é necessário")
    private List<ItemOrcadoRequestDTO> itens;
}
