package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.input.dto.response.DashboardResponseDTO;
import com.garagem52.adapter.input.dto.response.RelatorioFinanceiroResponseDTO;
import com.garagem52.adapter.output.persistence.entity.OrcamentoEntity;
import com.garagem52.adapter.output.persistence.repository.MongoOrcamentoRepository;
import com.garagem52.adapter.output.persistence.repository.MongoUserRepository;
import com.garagem52.domain.utils.enums.MotivoCancelamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardRepositoryAdapterTest {

    @Mock
    private MongoOrcamentoRepository orcamentoRepository;

    @Mock
    private MongoUserRepository userRepository;

    @Mock
    private MongoTemplate mongo;

    @InjectMocks
    private DashboardRepositoryAdapter adapter;

    private OrcamentoEntity finalizado;
    private OrcamentoEntity cancelado;

    @BeforeEach
    void setup() {
        finalizado = OrcamentoEntity.builder()
                .status(OrcamentoStatus.FINALIZADO)
                .valorTotal(1000.0)
                .valorMaoDeObra(600.0)
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
        when(orcamentoRepository.findAll())
                .thenReturn(List.of(finalizado, cancelado));

        when(mongo.count(
                any(Query.class),
                eq(Document.class),
                eq("cliente_veiculo")))
                .thenReturn(5L);

        DashboardResponseDTO result = adapter.buscarVisaoGeral("MES");

        assertNotNull(result);
        assertEquals(1L, result.getOrcamentosFechados());
        assertEquals(5L, result.getClientesCadastrados());
        assertEquals(1000.0, result.getFaturamentoTotal());

        assertFalse(result.getStatusOrcamentos().isEmpty());
        assertFalse(result.getMotivosCancelamento().isEmpty());
        assertFalse(result.getEvolucaoFaturamento().isEmpty());
    }

    @Test
    void deveBuscarRelatorioFinanceiro() {
        when(orcamentoRepository.findAll())
                .thenReturn(List.of(finalizado, cancelado));

        RelatorioFinanceiroResponseDTO result =
                adapter.buscarRelatorioFinanceiro("MES");

        assertNotNull(result);

        assertEquals(1000.0, result.getFaturamentoTotal());
        assertEquals(300.0, result.getTotalCancelado());

        // agora faturamentoLiquido = soma(valorMaoDeObra)
        assertEquals(600.0, result.getFaturamentoLiquido());

        assertFalse(result.getEvolucaoFaturamento().isEmpty());
        assertFalse(result.getFaturamentoVsCancelamento().isEmpty());
    }
}