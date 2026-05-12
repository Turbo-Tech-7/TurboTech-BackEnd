package com.garagem52.adapter.input.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * Preenchido pelo mecânico na tela "Criar Orçamento".
 *
 * O mecânico NÃO informa IDs diretamente.
 * O fluxo esperado é:
 *   1. Mecânico cadastra o cliente+veículo → recebe clienteVeiculoId
 *   2. Mecânico preenche este formulário informando apenas
 *      clienteVeiculoId, descrição do serviço, peças e mão de obra.
 *   3. O backend resolve veiculoId e servicoId a partir do clienteVeiculoId.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateOrcamentoRequestDTO {

    /**
     * ID retornado pelo endpoint POST /clientes-veiculos.
     * A partir dele o backend resolve o veículo vinculado.
     */
    @NotBlank(message = "ID do cadastro de cliente/veículo é obrigatório")
    private String clienteVeiculoId;

    @NotBlank(message = "Descrição do serviço é obrigatória")
    private String descricaoServico;

    @NotNull(message = "Valor da mão de obra é obrigatório")
    private Double valorMaoDeObra;

    @NotEmpty(message = "Ao menos um item é necessário")
    private List<ItemOrcadoRequestDTO> itens;
}
