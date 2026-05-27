package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.*;
import com.garagem52.ports.input.DashboardInputPort;
import com.garagem52.ports.output.DashboardOutputPort;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
public class DashboardService implements DashboardInputPort {

    private final DashboardOutputPort dashboardOutputPort;

    @Override
    public DashboardResponseDTO buscarVisaoGeral(String filtro) {
        return dashboardOutputPort.buscarVisaoGeral(filtro);
    }

    @Override
    public RelatorioFinanceiroResponseDTO buscarRelatorioFinanceiro(String filtro) {
        return dashboardOutputPort.buscarRelatorioFinanceiro(filtro);
    }

    private LocalDateTime[] resolveRange(String filtro) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from;
        if ("ano".equalsIgnoreCase(filtro)) {
            from = now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else {
            YearMonth ym = YearMonth.now();
            from = ym.atDay(1).atStartOfDay();
        }
        return new LocalDateTime[]{from, now};
    }

    @Override
    public RelatorioOrcamentoDTO getRelatorioOrcamentos(String filtro) {
        LocalDateTime[] range = resolveRange(filtro);
        LocalDateTime from = range[0], to = range[1];

        long total = dashboardOutputPort.countTotalOrcamentos(from, to);
        long cancelados = dashboardOutputPort.countOrcamentosByStatus("CANCELADO", from, to);
        long concluidos = dashboardOutputPort.countOrcamentosByStatus("CONCLUIDO", from, to);
        long abertos = dashboardOutputPort.countOrcamentosByStatus("ABERTO", from, to);

        List<RelatorioOrcamentoDTO.StatusItemDTO> statusList =
                dashboardOutputPort.groupOrcamentosByStatus(from, to).stream()
                        .map(d -> new RelatorioOrcamentoDTO.StatusItemDTO(
                                d.getString("_id"),
                                ((Number) d.get("count")).longValue()))
                        .toList();

        List<RelatorioOrcamentoDTO.MotivoItemDTO> motivoList =
                dashboardOutputPort.groupOrcamentosByMotivoCancelamento(from, to).stream()
                        .map(d -> new RelatorioOrcamentoDTO.MotivoItemDTO(
                                d.getString("_id"),
                                ((Number) d.get("count")).longValue()))
                        .toList();

        return new RelatorioOrcamentoDTO(total, cancelados, concluidos, abertos, statusList, motivoList);
    }

    @Override
    public RelatorioPecasDTO getRelatorioPecas(String filtro) {
        LocalDateTime[] range = resolveRange(filtro);
        LocalDateTime from = range[0], to = range[1];

        double valorGasto = dashboardOutputPort.sumValorItensPorPeriodo(from, to);
        long totalFornecedores = dashboardOutputPort.countFornecedoresDistintos(from, to);

        List<RelatorioPecasDTO.FornecedorItemDTO> fornecedores =
                dashboardOutputPort.groupItensByFornecedor(from, to).stream()
                        .map(d -> new RelatorioPecasDTO.FornecedorItemDTO(
                                d.getString("_id"),
                                ((Number) d.get("count")).longValue()))
                        .toList();

        String principalFornecedor = fornecedores.isEmpty() ? "-" : fornecedores.get(0).fornecedor();

        List<RelatorioPecasDTO.EvolucaoMensalDTO> evolucao =
                dashboardOutputPort.evolucaoMensalGastoPecas(from, to).stream()
                        .map(d -> new RelatorioPecasDTO.EvolucaoMensalDTO(
                                d.getString("mes"),
                                BigDecimal.valueOf(((Number) d.get("total")).doubleValue())))
                        .toList();

        return new RelatorioPecasDTO(
                BigDecimal.valueOf(valorGasto),
                totalFornecedores,
                principalFornecedor,
                fornecedores,
                evolucao
        );
    }

    @Override
    public RelatorioClientesDTO getRelatorioClientes(String filtro) {
        LocalDateTime[] range = resolveRange(filtro);
        LocalDateTime from = range[0], to = range[1];

        long totalClientes = dashboardOutputPort.countClientesDistintos(from, to);
        long recorrentes = dashboardOutputPort.countClientesRecorrentes(from, to);
        String maisPresente = dashboardOutputPort.findClienteMaisPresente(from, to);

        List<RelatorioClientesDTO.TopClienteDTO> top5 =
                dashboardOutputPort.topClientesPorFaturamento(from, to).stream()
                        .map(d -> new RelatorioClientesDTO.TopClienteDTO(
                                d.getString("_id"),
                                BigDecimal.valueOf(((Number) d.get("faturamento")).doubleValue())))
                        .toList();

        List<RelatorioClientesDTO.NovosClientesMesDTO> novosPorMes =
                dashboardOutputPort.novosClientesPorMes(from, to).stream()
                        .map(d -> new RelatorioClientesDTO.NovosClientesMesDTO(
                                d.getString("mes"),
                                ((Number) d.get("count")).longValue()))
                        .toList();

        List<Document> rankingDocs = dashboardOutputPort.rankingClientesPorOrcamentosConcluidos(from, to);
        List<RelatorioClientesDTO.RankingClienteDTO> ranking = new java.util.ArrayList<>();
        for (int i = 0; i < rankingDocs.size(); i++) {
            Document d = rankingDocs.get(i);
            ranking.add(new RelatorioClientesDTO.RankingClienteDTO(
                    i + 1,
                    d.getString("_id"),
                    ((Number) d.get("orcamentosConcluidos")).longValue(),
                    BigDecimal.valueOf(((Number) d.get("valorTotal")).doubleValue())
            ));
        }

        return new RelatorioClientesDTO(
                maisPresente == null ? "-" : maisPresente,
                totalClientes,
                recorrentes,
                top5,
                novosPorMes,
                ranking
        );
    }
}
