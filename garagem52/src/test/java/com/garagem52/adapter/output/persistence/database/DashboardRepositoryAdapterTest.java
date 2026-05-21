package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.input.dto.response.DashboardResponseDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO;
import com.garagem52.adapter.output.persistence.entity.OrcamentoEntity;
import com.garagem52.adapter.output.persistence.repository.MongoOrcamentoRepository;
import com.garagem52.adapter.output.persistence.repository.MongoUserRepository;
import com.garagem52.domain.utils.enums.MotivoCancelamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardRepositoryAdapterTest {

    @Mock
    private MongoOrcamentoRepository orcamentoRepository;

    @Mock
    private MongoUserRepository userRepository;

    @InjectMocks
    private DashboardRepositoryAdapter adapter;

    private OrcamentoEntity finalizado;
    private OrcamentoEntity cancelado;

    @BeforeEach
    void setup() {
        finalizado = OrcamentoEntity.builder()
                .status(OrcamentoStatus.FINALIZADO)
                .valorTotal(1000.0)
                .dataOrcamento(LocalDateTime.now())
                .build();

        cancelado = OrcamentoEntity.builder()
                .status(OrcamentoStatus.CANCELADO)
                .valorTotal(300.0)
                .motivoCancelamento(MotivoCancelamento.CLIENTE_DESISTIU)
                .dataOrcamento(LocalDateTime.now())
                .build();
    }

    @Test
    void deveBuscarVisaoGeral() {
        when(orcamentoRepository.findAll()).thenReturn(List.of(finalizado, cancelado));
        when(userRepository.count()).thenReturn(5L);

        DashboardResponseDTO result = adapter.buscarVisaoGeral("MES");

        assertEquals(1, result.getOrcamentosFechados());
        assertEquals(5, result.getClientesCadastrados());
        assertEquals(1000.0, result.getFaturamentoTotal());
        assertFalse(result.getStatusOrcamentos().isEmpty());
    }

    @Test
    void deveBuscarRelatorioFinanceiro() {
        when(orcamentoRepository.findAll()).thenReturn(List.of(finalizado, cancelado));

        RelatorioFinanceiroResponseDTO result =
                adapter.buscarRelatorioFinanceiro("MES");

        assertEquals(1000.0, result.getFaturamentoTotal());
        assertEquals(300.0, result.getTotalCancelado());
        assertEquals(700.0, result.getFaturamentoLiquido());
        assertFalse(result.getEvolucaoFaturamento().isEmpty());
    }
}