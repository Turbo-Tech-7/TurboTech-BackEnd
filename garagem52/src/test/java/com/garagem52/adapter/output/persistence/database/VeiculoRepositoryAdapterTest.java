package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.VeiculoEntity;
import com.garagem52.adapter.output.persistence.mapper.VeiculoPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.MongoVeiculoRepository;
import com.garagem52.domain.model.Veiculo;
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
class VeiculoRepositoryAdapterTest {

    @Mock
    private MongoVeiculoRepository repository;

    @Mock
    private VeiculoPersistenceMapper mapper;

    @InjectMocks
    private VeiculoRepositoryAdapter adapter;

    @Test
    void deveSalvarVeiculo() {
        Veiculo domain = Veiculo.builder()
                .id("1")
                .placa("ABC1234")
                .build();

        VeiculoEntity entity = VeiculoEntity.builder()
                .id("1")
                .placa("ABC1234")
                .build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Veiculo result = adapter.save(domain);

        assertEquals("ABC1234", result.getPlaca());
    }

    @Test
    void deveBuscarPorId() {
        Veiculo domain = Veiculo.builder()
                .id("1")
                .placa("ABC1234")
                .build();

        VeiculoEntity entity = VeiculoEntity.builder()
                .id("1")
                .placa("ABC1234")
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Veiculo> result = adapter.findById("1");

        assertTrue(result.isPresent());
    }

    @Test
    void deveBuscarTodos() {
        Veiculo domain = Veiculo.builder()
                .id("1")
                .placa("ABC1234")
                .build();

        VeiculoEntity entity = VeiculoEntity.builder()
                .id("1")
                .placa("ABC1234")
                .build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Veiculo> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void deveBuscarPorPlaca() {
        Veiculo domain = Veiculo.builder()
                .id("1")
                .placa("ABC1234")
                .build();

        VeiculoEntity entity = VeiculoEntity.builder()
                .id("1")
                .placa("ABC1234")
                .build();

        when(repository.findByPlaca("ABC1234"))
                .thenReturn(Optional.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Veiculo> result = adapter.findByPlaca("ABC1234");

        assertTrue(result.isPresent());
    }

    @Test
    void deveVerificarSeExistePorPlaca() {
        when(repository.existsByPlaca("ABC1234"))
                .thenReturn(true);

        boolean result = adapter.existsByPlaca("ABC1234");

        assertTrue(result);
    }

    @Test
    void deveDeletarPorId() {
        adapter.deleteById("1");

        verify(repository).deleteById("1");
    }
}