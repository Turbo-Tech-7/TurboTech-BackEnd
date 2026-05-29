package com.garagem52.adapter.input.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFinanceiroResponseDTO {

    private Double faturamentoTotal;
    private Double totalCancelado;

    private List<FaturamentoVsCancelamentoDTO> faturamentoVsCancelamento;
    private List<EvolucaoFaturamentoDTO> evolucaoFaturamento;

    @Data
    @Builder
    public static class FaturamentoVsCancelamentoDTO {
        private String periodo;
        private Double faturamento;
        private Double cancelado;
    }

    @Data
    @Builder
    public static class EvolucaoFaturamentoDTO {
        private String periodo;
        private Double valor;
    }
}
