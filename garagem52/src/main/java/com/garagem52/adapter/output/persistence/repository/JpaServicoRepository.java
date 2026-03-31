package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServicoRepository extends JpaRepository<ServicoEntity, Long> {
    List<ServicoEntity> findByVeiculoId(Long veiculoId);
    List<ServicoEntity> findByStatus(String status);
}
