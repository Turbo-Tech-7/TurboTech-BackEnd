package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.ClienteVeiculoEntity;
import com.garagem52.adapter.output.persistence.mapper.ClienteVeiculoMapper;
import com.garagem52.adapter.output.persistence.repository.MongoClienteVeiculoRepository;
import com.garagem52.domain.model.ClienteVeiculo;
import org.junit.jupiter.api.BeforeEach;
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
class ClienteVeiculoRepositoryAdapterTest {

    @Mock
    private MongoClienteVeiculoRepository repository;

    @Mock
    private ClienteVeiculoMapper mapper;

    @InjectMocks
    private ClienteVeiculoRepositoryAdapter adapter;

    private ClienteVeiculo domain;
    private ClienteVeiculoEntity entity;

    @BeforeEach
    void setup() {
        domain = ClienteVeiculo.builder().id("1").nomeCliente("João").build();
        entity = ClienteVeiculoEntity.builder().id("1").nomeCliente("João").build();
    }

    @Test
    void deveSalvarClienteVeiculo() {
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        ClienteVeiculo result = adapter.save(domain);

        assertNotNull(result);
        assertEquals("João", result.getNomeCliente());

        verify(repository).save(entity);
    }

    @Test
    void deveBuscarPorId() {
        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<ClienteVeiculo> result = adapter.findById("1");

        assertTrue(result.isPresent());
        assertEquals("João", result.get().getNomeCliente());
    }

    @Test
    void deveBuscarTodos() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<ClienteVeiculo> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void deveBuscarPorNomeCliente() {
        when(repository.findByNomeClienteContainingIgnoreCase("jo")).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<ClienteVeiculo> result = adapter.findByNomeCliente("jo");

        assertEquals(1, result.size());
    }

    @Test
    void deveBuscarPorPlaca() {
        when(repository.findByPlacaVeiculo("ABC1234")).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<ClienteVeiculo> result = adapter.findByPlaca("abc1234");

        assertEquals(1, result.size());

        verify(repository).findByPlacaVeiculo("ABC1234");
    }

    @Test
    void deveDeletarPorId() {
        adapter.deleteById("1");

        verify(repository).deleteById("1");
    }
}