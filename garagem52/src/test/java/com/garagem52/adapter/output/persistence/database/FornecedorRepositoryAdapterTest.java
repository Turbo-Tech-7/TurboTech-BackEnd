package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.FornecedorEntity;
import com.garagem52.adapter.output.persistence.mapper.FornecedorPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.MongoFornecedorRepository;
import com.garagem52.domain.model.Fornecedor;
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
class FornecedorRepositoryAdapterTest {

    @Mock
    private MongoFornecedorRepository repository;

    @Mock
    private FornecedorPersistenceMapper mapper;

    @InjectMocks
    private FornecedorRepositoryAdapter adapter;

    @Test
    void deveSalvarFornecedor() {
        Fornecedor domain = Fornecedor.builder().id("1").nome("Fornecedor").build();
        FornecedorEntity entity = FornecedorEntity.builder().id("1").nome("Fornecedor").build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Fornecedor result = adapter.save(domain);

        assertEquals("Fornecedor", result.getNome());
    }

    @Test
    void deveBuscarPorId() {
        Fornecedor domain = Fornecedor.builder().id("1").nome("Fornecedor").build();
        FornecedorEntity entity = FornecedorEntity.builder().id("1").nome("Fornecedor").build();

        when(repository.findById("1")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Fornecedor> result = adapter.findById("1");

        assertTrue(result.isPresent());
    }

    @Test
    void deveBuscarTodos() {
        Fornecedor domain = Fornecedor.builder().id("1").nome("Fornecedor").build();
        FornecedorEntity entity = FornecedorEntity.builder().id("1").nome("Fornecedor").build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Fornecedor> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void deveBuscarPorNome() {
        Fornecedor domain = Fornecedor.builder().id("1").nome("Fornecedor").build();
        FornecedorEntity entity = FornecedorEntity.builder().id("1").nome("Fornecedor").build();

        when(repository.findByNomeContainingIgnoreCase("for")).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Fornecedor> result = adapter.findByNome("for");

        assertEquals(1, result.size());
    }

    @Test
    void deveDeletarPorId() {
        adapter.deleteById("1");

        verify(repository).deleteById("1");
    }
}
