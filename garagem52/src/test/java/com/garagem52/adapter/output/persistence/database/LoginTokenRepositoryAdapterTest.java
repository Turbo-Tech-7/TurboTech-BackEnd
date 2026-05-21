package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.LoginTokenEntity;
import com.garagem52.adapter.output.persistence.repository.MongoLoginTokenRepository;
import com.garagem52.domain.model.LoginToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginTokenRepositoryAdapterTest {

    @Mock
    private MongoLoginTokenRepository repository;

    @InjectMocks
    private LoginTokenRepositoryAdapter adapter;

    @Test
    void deveSalvarToken() {
        LoginToken token = LoginToken.builder()
                .id("1")
                .userId("user")
                .token("abc")
                .expiresAt(LocalDateTime.now())
                .used(false)
                .build();

        LoginTokenEntity entity = LoginTokenEntity.builder()
                .id("1")
                .userId("user")
                .token("abc")
                .expiresAt(token.getExpiresAt())
                .used(false)
                .build();

        when(repository.save(any())).thenReturn(entity);

        LoginToken result = adapter.salvar(token);

        assertEquals("abc", result.getToken());
    }

    @Test
    void deveBuscarPorToken() {
        LoginTokenEntity entity = LoginTokenEntity.builder()
                .id("1")
                .token("abc")
                .build();

        when(repository.findByToken("abc")).thenReturn(Optional.of(entity));

        Optional<LoginToken> result = adapter.buscarPorToken("abc");

        assertTrue(result.isPresent());
    }

    @Test
    void deveDeletarPorUserId() {
        adapter.deletarPorUserId("1");

        verify(repository).deleteByUserId("1");
    }
}