package com.garagem52.adapter.input.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RelatorioFinanceiroResponseDTO {

    private Double faturamentoTotal;
    private Double totalCancelado;
    private Double faturamentoLiquido;

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
