package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.PasswordResetTokenEntity;
import com.garagem52.adapter.output.persistence.mapper.PasswordResetTokenMapper;
import com.garagem52.adapter.output.persistence.repository.MongoPasswordResetTokenRepository;
import com.garagem52.domain.model.PasswordResetToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenRepositoryAdapterTest {

    @Mock
    private MongoPasswordResetTokenRepository repository;

    @Mock
    private PasswordResetTokenMapper mapper;

    @InjectMocks
    private PasswordResetTokenRepositoryAdapter adapter;

    @Test
    void deveSalvarToken() {
        PasswordResetToken token = PasswordResetToken.builder()
                .id("1")
                .token("abc")
                .build();

        PasswordResetTokenEntity entity =
                PasswordResetTokenEntity.builder()
                        .id("1")
                        .token("abc")
                        .build();

        when(mapper.toEntity(token)).thenReturn(entity);

        adapter.salvar(token);

        verify(repository).save(entity);
    }

    @Test
    void deveBuscarPorToken() {
        PasswordResetToken token = PasswordResetToken.builder()
                .id("1")
                .token("abc")
                .build();

        PasswordResetTokenEntity entity =
                PasswordResetTokenEntity.builder()
                        .id("1")
                        .token("abc")
                        .build();

        when(repository.findByToken("abc"))
                .thenReturn(Optional.of(entity));

        when(mapper.toDomain(entity)).thenReturn(token);

        Optional<PasswordResetToken> result =
                adapter.buscarPorToken("abc");

        assertTrue(result.isPresent());
    }

    @Test
    void deveDeletarTokenQuandoIdNaoForNull() {
        PasswordResetToken token = PasswordResetToken.builder()
                .id("1")
                .build();

        adapter.deletar(token);

        verify(repository).deleteById("1");
    }

    @Test
    void naoDeveDeletarQuandoIdForNull() {
        PasswordResetToken token = PasswordResetToken.builder()
                .build();

        adapter.deletar(token);

        verify(repository, never()).deleteById(any());
    }
}