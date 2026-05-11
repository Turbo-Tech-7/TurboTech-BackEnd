package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.ServicoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoServicoRepository extends MongoRepository<ServicoEntity, String> {
    List<ServicoEntity> findByVeiculoId(String veiculoId);
    List<ServicoEntity> findByStatus(String status);
}
