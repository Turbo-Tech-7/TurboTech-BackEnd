package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.DashboardResponseDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO;
import com.garagem52.ports.input.DashboardInputPort;
import com.garagem52.ports.output.DashboardOutputPort;
import lombok.RequiredArgsConstructor;

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
}
