package com.garagem52.ports.output;

import com.garagem52.adapter.input.dto.response.DashboardResponseDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO;

public interface DashboardOutputPort {
    DashboardResponseDTO buscarVisaoGeral(String filtro);
    RelatorioFinanceiroResponseDTO buscarRelatorioFinanceiro(String filtro);
}
