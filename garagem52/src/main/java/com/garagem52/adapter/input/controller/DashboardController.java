package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.dto.response.*;
import com.garagem52.ports.input.DashboardInputPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "KPIs e gráficos para as telas de visão geral e relatório financeiro")
public class DashboardController {

    private final DashboardInputPort dashboardInputPort;

    /**
     * GET /dashboard/visao-geral?filtro=DIA
     * filtro: DIA | MES | ANO  (default: MES)
     */
    @GetMapping("/visao-geral")
    @Operation(summary = "Visão Geral",
               description = "Retorna orçamentos fechados, clientes, faturamento, " +
                             "status dos orçamentos (pizza), motivos de cancelamento (pizza) " +
                             "e evolução do faturamento (linha).")
    public ResponseEntity<DashboardResponseDTO> visaoGeral(
            @Parameter(description = "Período: DIA, MES ou ANO")
            @RequestParam(defaultValue = "MES") String filtro) {
        return ResponseEntity.ok(dashboardInputPort.buscarVisaoGeral(filtro.toUpperCase()));
    }

    /**
     * GET /dashboard/relatorio-financeiro?filtro=MES
     * filtro: DIA | MES | ANO  (default: MES)
     */
    @GetMapping("/relatorio-financeiro")
    @Operation(summary = "Relatório Financeiro",
               description = "Retorna faturamento total, total cancelado, faturamento líquido, " +
                             "faturamento vs cancelamento (barras) e evolução do faturamento (linha).")
    public ResponseEntity<RelatorioFinanceiroResponseDTO> relatorioFinanceiro(
            @Parameter(description = "Período: DIA, MES ou ANO")
            @RequestParam(defaultValue = "MES") String filtro) {
        return ResponseEntity.ok(dashboardInputPort.buscarRelatorioFinanceiro(filtro.toUpperCase()));
    }

    @GetMapping("/orcamentos")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            summary = "Relatório de Orçamentos",
            description = "Retorna totais e distribuição de status/motivos para a aba Orçamentos do Dashboard."
    )
    public ResponseEntity<RelatorioOrcamentoDTO> getRelatorioOrcamentos(
            @Parameter(description = "Período: 'mes' (padrão) ou 'ano'")
            @RequestParam(defaultValue = "mes") String filtro) {

        return ResponseEntity.ok(dashboardInputPort.getRelatorioOrcamentos(filtro));
    }

    @GetMapping("/pecas")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            summary = "Relatório de Peças",
            description = "Retorna gastos com peças, fornecedores e evolução mensal para a aba Peças do Dashboard."
    )
    public ResponseEntity<RelatorioPecasDTO> getRelatorioPecas(
            @Parameter(description = "Período: 'mes' (padrão) ou 'ano'")
            @RequestParam(defaultValue = "mes") String filtro) {

        return ResponseEntity.ok(dashboardInputPort.getRelatorioPecas(filtro));
    }

    @GetMapping("/clientes")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(
            summary = "Relatório de Clientes",
            description = "Retorna análise de clientes, faturamento e novos clientes para a aba Clientes do Dashboard."
    )
    public ResponseEntity<RelatorioClientesDTO> getRelatorioClientes(
            @Parameter(description = "Período: 'mes' (padrão) ou 'ano'")
            @RequestParam(defaultValue = "mes") String filtro) {

        return ResponseEntity.ok(dashboardInputPort.getRelatorioClientes(filtro));
    }
}
