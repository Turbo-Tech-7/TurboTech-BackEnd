package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.FornecedorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoFornecedorRepository extends MongoRepository<FornecedorEntity, String> {
    List<FornecedorEntity> findByNomeContainingIgnoreCase(String nome);
}
