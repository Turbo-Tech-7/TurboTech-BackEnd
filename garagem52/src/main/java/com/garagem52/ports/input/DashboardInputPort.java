package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.response.DashboardResponseDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO;

public interface DashboardInputPort {
    DashboardResponseDTO buscarVisaoGeral(String filtro);
    RelatorioFinanceiroResponseDTO buscarRelatorioFinanceiro(String filtro);
}
