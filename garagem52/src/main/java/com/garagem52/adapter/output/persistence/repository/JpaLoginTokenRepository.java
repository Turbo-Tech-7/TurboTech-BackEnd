package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.LoginTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaLoginTokenRepository extends JpaRepository<LoginTokenEntity, Long> {
    Optional<LoginTokenEntity> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM LoginTokenEntity t WHERE t.userId = :userId")
    void deleteByUserId(Long userId);
}
