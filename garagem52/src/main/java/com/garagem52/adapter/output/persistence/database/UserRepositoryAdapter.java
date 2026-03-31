package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.UserPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.JpaUserRepository;
import com.garagem52.domain.model.User;
import com.garagem52.ports.output.UserOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * HEXAGONAL — OUTBOUND ADAPTER (database layer)
 * Implementação concreta do UserOutputPort — a ponte entre o domínio e o MySQL.
 * Posição no hexágono:
 *   Domínio  → define UserOutputPort (output port — abstração)
 *   Este adapter → implementa UserOutputPort usando JPA + Hibernate + MySQL
 * O UserService conhece APENAS UserOutputPort (interface).
 * Trocar este adapter por um MongoUserRepositoryAdapter:
 *   1. Cria MongoUserRepositoryAdapter implementando UserOutputPort
 *   2. Atualiza o @Bean no BeanConfiguration
 *   3. Domínio e controllers: ZERO alterações
 * Responsabilidade desta camada:
 *   1. Recebe User (domínio) → converte para UserEntity (JPA) via mapper
 *   2. Delega ao JpaUserRepository (Hibernate/MySQL) → ORM executa o SQL
 *   3. Converte UserEntity → User (domínio) antes de retornar ao serviço
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserOutputPort {

    private final JpaUserRepository jpaRepository;      // Spring Data JPA (ORM)
    private final UserPersistenceMapper persistenceMapper;   // Domain ↔ Entity

    @Override
    public User save(User user) {
        var entity = persistenceMapper.toEntity(user);
        var saved  = jpaRepository.save(entity);         // Hibernate: INSERT ou UPDATE
        return persistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id)                // Hibernate: SELECT WHERE id=?
                .map(persistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)          // Hibernate: SELECT WHERE email=?
                .map(persistenceMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll()                   // Hibernate: SELECT *
                .stream()
                .map(persistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);                    // Hibernate: DELETE WHERE id=?
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);       // Hibernate: SELECT COUNT(*)
    }
}
