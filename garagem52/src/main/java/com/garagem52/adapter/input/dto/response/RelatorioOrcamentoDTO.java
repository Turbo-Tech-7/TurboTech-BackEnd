package com.garagem52.adapter.input.dto.response;

import java.util.List;

public record RelatorioOrcamentoDTO(
        long totalOrcamentos,
        long totalCancelados,
        long totalConcluidos,
        long totalAbertos,
        List<StatusItemDTO> statusOrcamentos,
        List<MotivoItemDTO> motivosCancelamentos
) {
    public record StatusItemDTO(String status, long quantidade) {}
    public record MotivoItemDTO(String motivo, long quantidade) {}
}
