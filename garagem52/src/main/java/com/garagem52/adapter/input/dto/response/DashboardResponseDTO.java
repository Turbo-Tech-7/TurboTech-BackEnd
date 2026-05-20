package com.garagem52.adapter.input.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardResponseDTO {

    private Long orcamentosFechados;
    private Long clientesCadastrados;
    private Double faturamentoTotal;

    /** ex: {"Aberto": 3, "Finalizado": 10, "Cancelado": 2} */
    private Map<String, Long> statusOrcamentos;

    /** ex: {"Cliente desistiu": 2, "Preço alto": 1} */
    private Map<String, Long> motivosCancelamento;

    private List<FaturamentoPorPeriodoDTO> evolucaoFaturamento;

    @Data
    @Builder
    public static class FaturamentoPorPeriodoDTO {
        private String periodo; // "2025-01-15" | "2025-01" | "2025"
        private Double valor;
    }
}
