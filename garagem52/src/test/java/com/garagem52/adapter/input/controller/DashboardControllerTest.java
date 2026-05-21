package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.dto.response.DashboardResponseDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO;
import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.DashboardInputPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardInputPort dashboardInputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void deveBuscarVisaoGeral() throws Exception {

        Mockito.when(dashboardInputPort.buscarVisaoGeral("MES"))
                .thenReturn(new DashboardResponseDTO());

        mockMvc.perform(get("/dashboard/visao-geral"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarRelatorioFinanceiro() throws Exception {

        Mockito.when(dashboardInputPort.buscarRelatorioFinanceiro("MES"))
                .thenReturn(new RelatorioFinanceiroResponseDTO());

        mockMvc.perform(get("/dashboard/relatorio-financeiro"))
                .andExpect(status().isOk());
    }
}