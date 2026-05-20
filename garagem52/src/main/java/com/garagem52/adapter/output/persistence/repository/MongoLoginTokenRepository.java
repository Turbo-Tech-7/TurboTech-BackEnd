package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.LoginTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoLoginTokenRepository extends MongoRepository<LoginTokenEntity, String> {
    Optional<LoginTokenEntity> findByToken(String token);
    void deleteByUserId(String userId);
}
