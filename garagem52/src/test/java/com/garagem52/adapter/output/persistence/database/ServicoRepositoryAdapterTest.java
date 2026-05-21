package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.ServicoEntity;
import com.garagem52.adapter.output.persistence.mapper.ServicoMapper;
import com.garagem52.adapter.output.persistence.repository.MongoServicoRepository;
import com.garagem52.domain.model.Servico;
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
class ServicoRepositoryAdapterTest {

    @Mock
    private MongoServicoRepository repository;

    @Mock
    private ServicoMapper mapper;

    @InjectMocks
    private ServicoRepositoryAdapter adapter;

    @Test
    void deveSalvarServico() {
        Servico domain = Servico.builder()
                .id("1")
                .status("ABERTO")
                .build();

        ServicoEntity entity = ServicoEntity.builder()
                .id("1")
                .status("ABERTO")
                .build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Servico result = adapter.save(domain);

        assertNotNull(result);
        assertEquals("ABERTO", result.getStatus());
    }

    @Test
    void deveBuscarPorId() {
        Servico domain = Servico.builder()
                .id("1")
                .status("ABERTO")
                .build();

        ServicoEntity entity = ServicoEntity.builder()
                .id("1")
                .status("ABERTO")
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Servico> result = adapter.findById("1");

        assertTrue(result.isPresent());
    }

    @Test
    void deveBuscarTodos() {
        Servico domain = Servico.builder()
                .id("1")
                .status("ABERTO")
                .build();

        ServicoEntity entity = ServicoEntity.builder()
                .id("1")
                .status("ABERTO")
                .build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Servico> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void deveBuscarPorVeiculoId() {
        Servico domain = Servico.builder()
                .id("1")
                .veiculoId("v1")
                .build();

        ServicoEntity entity = ServicoEntity.builder()
                .id("1")
                .veiculoId("v1")
                .build();

        when(repository.findByVeiculoId("v1"))
                .thenReturn(List.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Servico> result = adapter.findByVeiculoId("v1");

        assertEquals(1, result.size());
    }

    @Test
    void deveBuscarPorStatus() {
        Servico domain = Servico.builder()
                .id("1")
                .status("FINALIZADO")
                .build();

        ServicoEntity entity = ServicoEntity.builder()
                .id("1")
                .status("FINALIZADO")
                .build();

        when(repository.findByStatus("FINALIZADO"))
                .thenReturn(List.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Servico> result = adapter.findByStatus("FINALIZADO");

        assertEquals(1, result.size());
    }

    @Test
    void deveDeletarPorId() {
        adapter.deleteById("1");

        verify(repository).deleteById("1");
    }
}