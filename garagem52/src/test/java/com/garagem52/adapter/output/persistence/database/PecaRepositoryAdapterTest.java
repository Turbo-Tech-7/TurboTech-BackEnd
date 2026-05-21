package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.PecaEntity;
import com.garagem52.adapter.output.persistence.mapper.PecaPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.MongoPecaRepository;
import com.garagem52.domain.model.Peca;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PecaRepositoryAdapterTest {

    @Mock
    private MongoPecaRepository repository;

    @Mock
    private PecaPersistenceMapper mapper;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private PecaRepositoryAdapter adapter;

    @Test
    void deveBuscarPorNome() {
        PecaEntity entity = PecaEntity.builder()
                .id("1")
                .nomePeca("Filtro de Óleo")
                .build();

        Peca domain = Peca.builder()
                .id("1")
                .nome("Filtro de Óleo")
                .build();

        when(mongoTemplate.find(any(Query.class), eq(PecaEntity.class)))
                .thenReturn(List.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Peca> result = adapter.findByNome("Filtro");

        assertEquals(1, result.size());
        assertEquals("Filtro de Óleo", result.get(0).getNome());

        verify(mongoTemplate).find(any(Query.class), eq(PecaEntity.class));
    }

    @Test
    void deveBuscarPorPreco() {
        PecaEntity entity = PecaEntity.builder()
                .id("1")
                .nomePeca("Filtro")
                .precoPeca(100.0)
                .build();

        Peca domain = Peca.builder()
                .id("1")
                .nome("Filtro")
                .valor(100.0)
                .build();

        when(repository.findByPrecoPecaLessThanEqual(100.0))
                .thenReturn(List.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Peca> result = adapter.findByPreco(100.0);

        assertEquals(1, result.size());
        assertEquals(100.0, result.get(0).getValor());
    }

    @Test
    void deveBuscarPorId() {
        PecaEntity entity = PecaEntity.builder()
                .id("1")
                .nomePeca("Filtro")
                .build();

        Peca domain = Peca.builder()
                .id("1")
                .nome("Filtro")
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Peca> result = adapter.findById("1");

        assertTrue(result.isPresent());
        assertEquals("Filtro", result.get().getNome());
    }
}