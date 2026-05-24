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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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
    private final MongoTemplate mongo;

    @Override
    public DashboardResponseDTO buscarVisaoGeral(String filtro) {
        List<OrcamentoEntity> noPeriodo = filtrar(orcamentoRepository.findAll(), filtro);

        long orcamentosFechados = noPeriodo.stream()
                .filter(o -> OrcamentoStatus.FINALIZADO.equals(o.getStatus())).count();

        long clientes = mongo.count(
                new Query(),
                Document.class, "cliente_veiculo");

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

    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Criteria dataCriteria(LocalDateTime from, LocalDateTime to) {
        return Criteria.where("data_orcamento")
                .gte(toDate(from))
                .lte(toDate(to));
    }

    // ── Orçamentos ─────────────────────────────────────────────────────────

    @Override
    public long countTotalOrcamentos(LocalDateTime from, LocalDateTime to) {
        return mongo.count(
                new org.springframework.data.mongodb.core.query.Query(dataCriteria(from, to)),
                Document.class, "orcamento");
    }

    @Override
    public long countOrcamentosByStatus(String status, LocalDateTime from, LocalDateTime to) {
        return mongo.count(
                new org.springframework.data.mongodb.core.query.Query(
                        dataCriteria(from, to).and("status").is(status)),
                Document.class, "orcamento");
    }

    @Override
    public List<Document> groupOrcamentosByStatus(LocalDateTime from, LocalDateTime to) {
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to)),
                group("status").count().as("count"),
                sort(org.springframework.data.domain.Sort.Direction.DESC, "count")
        );
        return mongo.aggregate(agg, "orcamento", Document.class).getMappedResults();
    }

    @Override
    public List<Document> groupOrcamentosByMotivoCancelamento(LocalDateTime from, LocalDateTime to) {
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to)
                        .and("status").is("CANCELADO")
                        .and("motivo_cancelamento").ne(null)),
                group("motivo_cancelamento").count().as("count"),
                sort(org.springframework.data.domain.Sort.Direction.DESC, "count")
        );
        return mongo.aggregate(agg, "orcamento", Document.class).getMappedResults();
    }

    // ── Peças ──────────────────────────────────────────────────────────────

    @Override
    public double sumValorItensPorPeriodo(LocalDateTime from, LocalDateTime to) {
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to)),
                unwind("itens"),
                match(Criteria.where("itens.valor").ne(null)),
                group().sum(
                        ArithmeticOperators.Multiply.valueOf("itens.valor")
                                .multiplyBy("itens.quantidade")
                ).as("total"),
                project("total").andExclude("_id")
        );
        Document result = mongo.aggregate(agg, "orcamento", Document.class)
                .getUniqueMappedResult();
        if (result == null) return 0.0;
        Object total = result.get("total");
        return total == null ? 0.0 : ((Number) total).doubleValue();
    }

    @Override
    public List<Document> groupItensByFornecedor(LocalDateTime from, LocalDateTime to) {
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to)),
                unwind("itens"),
                match(Criteria.where("itens.fornecedor").ne(null)),
                group("itens.fornecedor").count().as("count"),
                sort(org.springframework.data.domain.Sort.Direction.DESC, "count"),
                limit(10)
        );
        return mongo.aggregate(agg, "orcamento", Document.class).getMappedResults();
    }

    @Override
    public long countFornecedoresDistintos(LocalDateTime from, LocalDateTime to) {
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to)),
                unwind("itens"),
                match(Criteria.where("itens.fornecedor").ne(null)),
                group("itens.fornecedor"),
                count().as("total")
        );
        Document result = mongo.aggregate(agg, "orcamento", Document.class)
                .getUniqueMappedResult();
        if (result == null) return 0L;
        Object total = result.get("total");
        return total == null ? 0L : ((Number) total).longValue();
    }

    @Override
    public List<Document> evolucaoMensalGastoPecas(LocalDateTime from, LocalDateTime to) {
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to)),
                unwind("itens"),
                match(Criteria.where("itens.valor").ne(null)),
                project()
                        .andExpression("year(data_orcamento)").as("ano")
                        .andExpression("month(data_orcamento)").as("mes_num")
                        .and(ArithmeticOperators.Multiply.valueOf("itens.valor")
                                .multiplyBy("itens.quantidade")).as("valorItem"),
                group(Fields.fields("ano", "mes_num"))
                        .sum("valorItem").as("total"),
                sort(org.springframework.data.domain.Sort.Direction.ASC, "_id.ano", "_id.mes_num"),
                project()
                        .andExpression("concat(toString(_id.ano), '-', toString(_id.mes_num))").as("mes")
                        .and("total").as("total")
                        .andExclude("_id")
        );
        return mongo.aggregate(agg, "orcamento", Document.class).getMappedResults();
    }

    // ── Clientes ───────────────────────────────────────────────────────────

    @Override
    public long countClientesDistintos(LocalDateTime from, LocalDateTime to) {
        Criteria criteria = Criteria.where("data_inclusao")
                .gte(toDate(from))
                .lte(toDate(to));
        Aggregation agg = newAggregation(
                match(criteria),
                group("nome_cliente"),
                count().as("total")
        );
        Document result = mongo.aggregate(agg, "cliente_veiculo", Document.class)
                .getUniqueMappedResult();
        if (result == null) return 0L;
        Object total = result.get("total");
        return total == null ? 0L : ((Number) total).longValue();
    }

    @Override
    public long countClientesRecorrentes(LocalDateTime from, LocalDateTime to) {
        // Clientes com mais de 1 orçamento no período
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to).and("nome_cliente").ne(null)),
                group("nome_cliente").count().as("qtd"),
                match(Criteria.where("qtd").gt(1)),
                count().as("total")
        );
        Document result = mongo.aggregate(agg, "orcamento", Document.class)
                .getUniqueMappedResult();
        if (result == null) return 0L;
        Object total = result.get("total");
        return total == null ? 0L : ((Number) total).longValue();
    }

    @Override
    public String findClienteMaisPresente(LocalDateTime from, LocalDateTime to) {
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to).and("nome_cliente").ne(null)),
                group("nome_cliente").count().as("count"),
                sort(org.springframework.data.domain.Sort.Direction.DESC, "count"),
                limit(1)
        );
        Document result = mongo.aggregate(agg, "orcamento", Document.class)
                .getUniqueMappedResult();
        return result == null ? null : result.getString("_id");
    }

    @Override
    public List<Document> topClientesPorFaturamento(LocalDateTime from, LocalDateTime to) {
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to)
                        .and("status").is("CONCLUIDO")
                        .and("nome_cliente").ne(null)),
                group("nome_cliente").sum("valor_total").as("faturamento"),
                sort(org.springframework.data.domain.Sort.Direction.DESC, "faturamento"),
                limit(5)
        );
        return mongo.aggregate(agg, "orcamento", Document.class).getMappedResults();
    }

    @Override
    public List<Document> novosClientesPorMes(LocalDateTime from, LocalDateTime to) {
        Criteria criteria = Criteria.where("data_inclusao")
                .gte(toDate(from))
                .lte(toDate(to));
        Aggregation agg = newAggregation(
                match(criteria),
                project()
                        .andExpression("year(data_inclusao)").as("ano")
                        .andExpression("month(data_inclusao)").as("mes_num"),
                group(Fields.fields("ano", "mes_num")).count().as("count"),
                sort(org.springframework.data.domain.Sort.Direction.ASC, "_id.ano", "_id.mes_num"),
                project()
                        .andExpression("concat(toString(_id.ano), '-', toString(_id.mes_num))").as("mes")
                        .and("count").as("count")
                        .andExclude("_id")
        );
        return mongo.aggregate(agg, "cliente_veiculo", Document.class).getMappedResults();
    }

    @Override
    public List<Document> rankingClientesPorOrcamentosConcluidos(LocalDateTime from, LocalDateTime to) {
        Aggregation agg = newAggregation(
                match(dataCriteria(from, to)
                        .and("status").is("CONCLUIDO")
                        .and("nome_cliente").ne(null)),
                group("nome_cliente")
                        .count().as("orcamentosConcluidos")
                        .sum("valor_total").as("valorTotal"),
                sort(org.springframework.data.domain.Sort.Direction.DESC, "orcamentosConcluidos")
        );
        return mongo.aggregate(agg, "orcamento", Document.class).getMappedResults();
    }
}
