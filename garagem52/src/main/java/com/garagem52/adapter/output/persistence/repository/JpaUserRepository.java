package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ADAPTER/OUTPUT — SPRING DATA JPA REPOSITORY
 * Interface implementada automaticamente pelo Hibernate em tempo de execução.
 * ORM em ação:
 *   JpaRepository<UserEntity, Long> → CRUD completo gerado pelo Spring Data
 *   findByEmail   → traduzido para: SELECT * FROM users WHERE email = ?
 *   existsByEmail → traduzido para: SELECT COUNT(*) FROM users WHERE email = ?
 * Esta interface NUNCA é exposta ao domínio.
 * O domínio acessa persistência somente via UserOutputPort (output port).
 * O UserRepositoryAdapter (database/) é o único que usa esta interface.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
