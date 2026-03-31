package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.FornecedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaFornecedorRepository extends JpaRepository<FornecedorEntity, Long> {

    List<FornecedorEntity> findByNomeContainingIgnoreCase(String nome);

    List<FornecedorEntity> findByCnpj(String cnpj);
}