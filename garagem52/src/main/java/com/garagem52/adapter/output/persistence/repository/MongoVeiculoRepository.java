package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.VeiculoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoVeiculoRepository extends MongoRepository<VeiculoEntity, String> {
    Optional<VeiculoEntity> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
}
