package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.UserEntity;
import com.garagem52.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.MongoUserRepository;
import com.garagem52.domain.model.User;
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
class UserRepositoryAdapterTest {

    @Mock
    private MongoUserRepository repository;

    @Mock
    private UserPersistenceMapper mapper;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @Test
    void deveSalvarUser() {
        User domain = User.builder()
                .id("1")
                .email("teste@email.com")
                .build();

        UserEntity entity = UserEntity.builder()
                .id("1")
                .email("teste@email.com")
                .build();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        User result = adapter.save(domain);

        assertEquals("teste@email.com", result.getEmail());
    }

    @Test
    void deveBuscarPorId() {
        User domain = User.builder()
                .id("1")
                .email("teste@email.com")
                .build();

        UserEntity entity = UserEntity.builder()
                .id("1")
                .email("teste@email.com")
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<User> result = adapter.findById("1");

        assertTrue(result.isPresent());
    }

    @Test
    void deveBuscarPorEmail() {
        User domain = User.builder()
                .id("1")
                .email("teste@email.com")
                .build();

        UserEntity entity = UserEntity.builder()
                .id("1")
                .email("teste@email.com")
                .build();

        when(repository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(entity));

        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<User> result =
                adapter.findByEmail("teste@email.com");

        assertTrue(result.isPresent());
    }

    @Test
    void deveBuscarTodos() {
        User domain = User.builder()
                .id("1")
                .email("teste@email.com")
                .build();

        UserEntity entity = UserEntity.builder()
                .id("1")
                .email("teste@email.com")
                .build();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<User> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void deveVerificarSeExistePorEmail() {
        when(repository.existsByEmail("teste@email.com"))
                .thenReturn(true);

        boolean result = adapter.existsByEmail("teste@email.com");

        assertTrue(result);
    }

    @Test
    void deveDeletarPorId() {
        adapter.deleteById("1");

        verify(repository).deleteById("1");
    }
}