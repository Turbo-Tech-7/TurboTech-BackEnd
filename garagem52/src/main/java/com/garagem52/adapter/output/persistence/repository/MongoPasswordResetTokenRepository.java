package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.PasswordResetTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoPasswordResetTokenRepository extends MongoRepository<PasswordResetTokenEntity, String> {
    Optional<PasswordResetTokenEntity> findByToken(String token);
}
