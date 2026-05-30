package com.garagem52.ports.output;

import com.garagem52.adapter.input.dto.response.DashboardResponseDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.List;

public interface DashboardOutputPort {
    DashboardResponseDTO buscarVisaoGeral(String filtro);
    RelatorioFinanceiroResponseDTO buscarRelatorioFinanceiro(String filtro);
    long countOrcamentosByStatus(String status, LocalDateTime from, LocalDateTime to);
    long countTotalOrcamentos(LocalDateTime from, LocalDateTime to);
    List<Document> groupOrcamentosByStatus(LocalDateTime from, LocalDateTime to);
    List<Document> groupOrcamentosByMotivoCancelamento(LocalDateTime from, LocalDateTime to);
    double sumValorItensPorPeriodo(LocalDateTime from, LocalDateTime to);
    double sumMaoDeObraFinalizados(LocalDateTime from, LocalDateTime to);
    List<Document> groupItensByFornecedor(LocalDateTime from, LocalDateTime to);
    long countFornecedoresDistintos(LocalDateTime from, LocalDateTime to);
    List<Document> evolucaoMensalGastoPecas(LocalDateTime from, LocalDateTime to);
    long countClientesDistintos(LocalDateTime from, LocalDateTime to);
    long countClientesRecorrentes(LocalDateTime from, LocalDateTime to);
    String findClienteMaisPresente(LocalDateTime from, LocalDateTime to);
    List<Document> topClientesPorFaturamento(LocalDateTime from, LocalDateTime to);
    List<Document> novosClientesPorMes(LocalDateTime from, LocalDateTime to);
    List<Document> rankingClientesPorOrcamentosConcluidos(LocalDateTime from, LocalDateTime to);
}
