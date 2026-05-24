package com.garagem52.adapter.input.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioClientesDTO(
        String clienteMaisPresente,
        long totalClientes,
        long clientesRecorrentes,
        List<TopClienteDTO> top5ClientesPorFaturamento,
        List<NovosClientesMesDTO> novosClientesPorMes,
        List<RankingClienteDTO> rankingClientesPorOrcamentosConcluidos
) {
    public record TopClienteDTO(String nome, BigDecimal faturamento) {}

    public record NovosClientesMesDTO(String mes, long quantidade) {}

    public record RankingClienteDTO(
            int posicao,
            String nome,
            long orcamentosConcluidos,
            BigDecimal valorTotal
    ) {}
}
