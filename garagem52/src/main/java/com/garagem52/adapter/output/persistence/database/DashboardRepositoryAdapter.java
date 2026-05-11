package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.input.dto.response.DashboardResponseDTO;
import com.garagem52.adapter.input.dto.response.DashboardResponseDTO.FaturamentoPorPeriodoDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO.EvolucaoFaturamentoDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO.FaturamentoVsCancelamentoDTO;
import com.garagem52.adapter.output.persistence.entity.OrcamentoEntity;
import com.garagem52.adapter.output.persistence.repository.MongoOrcamentoRepository;
import com.garagem52.adapter.output.persistence.repository.MongoUserRepository;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import com.garagem52.ports.output.DashboardOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Filtros:
 *   "DIA" → apenas hoje
 *   "MES" → mês corrente
 *   "ANO" → ano corrente
 */
@Component
@RequiredArgsConstructor
public class DashboardRepositoryAdapter implements DashboardOutputPort {

    private final MongoOrcamentoRepository orcamentoRepository;
    private final MongoUserRepository userRepository;

    @Override
    public DashboardResponseDTO buscarVisaoGeral(String filtro) {
        List<OrcamentoEntity> noPeriodo = filtrar(orcamentoRepository.findAll(), filtro);

        long orcamentosFechados = noPeriodo.stream()
                .filter(o -> OrcamentoStatus.FINALIZADO.equals(o.getStatus())).count();

        long clientes = userRepository.count();

        double faturamento = somarFinalizados(noPeriodo);

        Map<String, Long> statusMap = noPeriodo.stream()
                .filter(o -> o.getStatus() != null)
                .collect(Collectors.groupingBy(o -> o.getStatus().getDescricao(), Collectors.counting()));

        Map<String, Long> motivosMap = noPeriodo.stream()
                .filter(o -> OrcamentoStatus.CANCELADO.equals(o.getStatus()) && o.getMotivoCancelamento() != null)
                .collect(Collectors.groupingBy(o -> o.getMotivoCancelamento().getDescricao(), Collectors.counting()));

        return DashboardResponseDTO.builder()
                .orcamentosFechados(orcamentosFechados)
                .clientesCadastrados(clientes)
                .faturamentoTotal(faturamento)
                .statusOrcamentos(statusMap)
                .motivosCancelamento(motivosMap)
                .evolucaoFaturamento(buildEvolucao(noPeriodo, filtro))
                .build();
    }

    @Override
    public RelatorioFinanceiroResponseDTO buscarRelatorioFinanceiro(String filtro) {
        List<OrcamentoEntity> noPeriodo = filtrar(orcamentoRepository.findAll(), filtro);

        double faturamentoTotal = somarFinalizados(noPeriodo);

        double totalCancelado = noPeriodo.stream()
                .filter(o -> OrcamentoStatus.CANCELADO.equals(o.getStatus()))
                .mapToDouble(o -> o.getValorTotal() != null ? o.getValorTotal() : 0.0).sum();

        List<EvolucaoFaturamentoDTO> evolucao = noPeriodo.stream()
                .filter(o -> OrcamentoStatus.FINALIZADO.equals(o.getStatus()) && o.getDataOrcamento() != null)
                .collect(Collectors.groupingBy(
                        o -> formatar(o.getDataOrcamento(), filtro),
                        Collectors.summingDouble(o -> o.getValorTotal() != null ? o.getValorTotal() : 0.0)))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> EvolucaoFaturamentoDTO.builder().periodo(e.getKey()).valor(e.getValue()).build())
                .collect(Collectors.toList());

        return RelatorioFinanceiroResponseDTO.builder()
                .faturamentoTotal(faturamentoTotal)
                .totalCancelado(totalCancelado)
                .faturamentoLiquido(faturamentoTotal - totalCancelado)
                .faturamentoVsCancelamento(buildFaturamentoVsCancelamento(noPeriodo, filtro))
                .evolucaoFaturamento(evolucao)
                .build();
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private List<OrcamentoEntity> filtrar(List<OrcamentoEntity> todos, String filtro) {
        LocalDate hoje = LocalDate.now();
        return todos.stream().filter(o -> {
            if (o.getDataOrcamento() == null) return false;
            LocalDate d = o.getDataOrcamento().toLocalDate();
            return switch (filtro.toUpperCase()) {
                case "DIA" -> d.equals(hoje);
                case "MES" -> d.getYear() == hoje.getYear() && d.getMonth() == hoje.getMonth();
                case "ANO" -> d.getYear() == hoje.getYear();
                default    -> true;
            };
        }).collect(Collectors.toList());
    }

    private String formatar(LocalDateTime data, String filtro) {
        return switch (filtro.toUpperCase()) {
            case "DIA" -> data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            case "ANO" -> data.format(DateTimeFormatter.ofPattern("yyyy"));
            default    -> data.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        };
    }

    private double somarFinalizados(List<OrcamentoEntity> orcamentos) {
        return orcamentos.stream()
                .filter(o -> OrcamentoStatus.FINALIZADO.equals(o.getStatus()))
                .mapToDouble(o -> o.getValorTotal() != null ? o.getValorTotal() : 0.0).sum();
    }

    private List<FaturamentoPorPeriodoDTO> buildEvolucao(List<OrcamentoEntity> orcamentos, String filtro) {
        return orcamentos.stream()
                .filter(o -> OrcamentoStatus.FINALIZADO.equals(o.getStatus()) && o.getDataOrcamento() != null)
                .collect(Collectors.groupingBy(
                        o -> formatar(o.getDataOrcamento(), filtro),
                        Collectors.summingDouble(o -> o.getValorTotal() != null ? o.getValorTotal() : 0.0)))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> FaturamentoPorPeriodoDTO.builder().periodo(e.getKey()).valor(e.getValue()).build())
                .collect(Collectors.toList());
    }

    private List<FaturamentoVsCancelamentoDTO> buildFaturamentoVsCancelamento(
            List<OrcamentoEntity> orcamentos, String filtro) {

        Map<String, Double> fat = orcamentos.stream()
                .filter(o -> OrcamentoStatus.FINALIZADO.equals(o.getStatus()) && o.getDataOrcamento() != null)
                .collect(Collectors.groupingBy(
                        o -> formatar(o.getDataOrcamento(), filtro),
                        Collectors.summingDouble(o -> o.getValorTotal() != null ? o.getValorTotal() : 0.0)));

        Map<String, Double> can = orcamentos.stream()
                .filter(o -> OrcamentoStatus.CANCELADO.equals(o.getStatus()) && o.getDataOrcamento() != null)
                .collect(Collectors.groupingBy(
                        o -> formatar(o.getDataOrcamento(), filtro),
                        Collectors.summingDouble(o -> o.getValorTotal() != null ? o.getValorTotal() : 0.0)));

        Set<String> periodos = new TreeSet<>();
        periodos.addAll(fat.keySet());
        periodos.addAll(can.keySet());

        return periodos.stream()
                .map(p -> FaturamentoVsCancelamentoDTO.builder()
                        .periodo(p)
                        .faturamento(fat.getOrDefault(p, 0.0))
                        .cancelado(can.getOrDefault(p, 0.0))
                        .build())
                .collect(Collectors.toList());
    }
}
