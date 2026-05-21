package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.ClienteVeiculoEntity;
import com.garagem52.adapter.output.persistence.entity.OrcamentoEntity;
import com.garagem52.adapter.output.persistence.entity.VeiculoEntity;
import com.garagem52.adapter.output.persistence.mapper.ClienteVeiculoMapper;
import com.garagem52.adapter.output.persistence.mapper.OrcamentoMapper;
import com.garagem52.adapter.output.persistence.repository.MongoClienteVeiculoRepository;
import com.garagem52.adapter.output.persistence.repository.MongoOrcamentoRepository;
import com.garagem52.adapter.output.persistence.repository.MongoVeiculoRepository;
import com.garagem52.domain.model.ClienteVeiculo;
import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrcamentoRepositoryAdapterTest {

    @Mock
    private MongoOrcamentoRepository repository;

    @Mock
    private MongoVeiculoRepository veiculoRepository;

    @Mock
    private MongoClienteVeiculoRepository clienteVeiculoRepository;

    @Mock
    private OrcamentoMapper mapper;

    @Mock
    private ClienteVeiculoMapper clienteVeiculoMapper;

    @InjectMocks
    private OrcamentoRepositoryAdapter adapter;

    @Test
    void deveSalvarOrcamento() {
        Orcamento domain = Orcamento.builder()
                .id("1")
                .veiculoId("v1")
                .clienteVeiculoId("c1")
                .build();

        OrcamentoEntity entity = OrcamentoEntity.builder().id("1").build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        when(veiculoRepository.findById("v1"))
                .thenReturn(Optional.of(VeiculoEntity.builder().id("v1").build()));

        when(clienteVeiculoRepository.findById("c1"))
                .thenReturn(Optional.of(ClienteVeiculoEntity.builder()
                        .id("c1")
                        .nomeCliente("João")
                        .build()));

        when(clienteVeiculoMapper.toDomain(any()))
                .thenReturn(ClienteVeiculo.builder().id("c1").build());

        Orcamento result = adapter.save(domain);

        assertNotNull(result.getVeiculo());
        assertNotNull(result.getClienteVeiculo());
    }

    @Test
    void deveBuscarPorStatus() {
        Orcamento domain = Orcamento.builder().id("1").build();
        OrcamentoEntity entity = OrcamentoEntity.builder().id("1").build();

        when(repository.findByStatus(OrcamentoStatus.FINALIZADO))
                .thenReturn(List.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Orcamento> result =
                adapter.findByStatus(OrcamentoStatus.FINALIZADO);

        assertEquals(1, result.size());
    }

    @Test
    void deveDeletarPorId() {
        adapter.deleteById("1");

        verify(repository).deleteById("1");
    }
}