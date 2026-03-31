package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.FornecedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaFornecedorRepository extends JpaRepository<FornecedorEntity, Long> {
    List<FornecedorEntity> findByNomeContainingIgnoreCase(String nome);
}
