package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.DashboardResponseDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO;
import com.garagem52.ports.output.DashboardOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private DashboardOutputPort outputPort;

    @InjectMocks
    private DashboardService service;

    @Test
    void deveBuscarVisaoGeral() {
        DashboardResponseDTO dto = DashboardResponseDTO.builder()
                .clientesCadastrados(10L)
                .build();

        when(outputPort.buscarVisaoGeral("MES"))
                .thenReturn(dto);

        DashboardResponseDTO result =
                service.buscarVisaoGeral("MES");

        assertEquals(10L, result.getClientesCadastrados());
    }

    @Test
    void deveBuscarRelatorioFinanceiro() {
        RelatorioFinanceiroResponseDTO dto =
                RelatorioFinanceiroResponseDTO.builder()
                        .faturamentoTotal(1000.0)
                        .build();

        when(outputPort.buscarRelatorioFinanceiro("MES"))
                .thenReturn(dto);

        RelatorioFinanceiroResponseDTO result =
                service.buscarRelatorioFinanceiro("MES");

        assertEquals(1000.0, result.getFaturamentoTotal());
    }
}