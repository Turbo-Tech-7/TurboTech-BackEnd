package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.response.*;

public interface DashboardInputPort {
    DashboardResponseDTO buscarVisaoGeral(String filtro);
    RelatorioFinanceiroResponseDTO buscarRelatorioFinanceiro(String filtro);
    RelatorioOrcamentoDTO getRelatorioOrcamentos(String filtro);
    RelatorioPecasDTO getRelatorioPecas(String filtro);
    RelatorioClientesDTO getRelatorioClientes(String filtro);
}
