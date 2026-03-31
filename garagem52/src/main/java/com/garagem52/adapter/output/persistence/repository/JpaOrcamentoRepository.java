package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.OrcamentoEntity;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOrcamentoRepository extends JpaRepository<OrcamentoEntity, Long> {
    List<OrcamentoEntity> findByVeiculoId(Long veiculoId);
    List<OrcamentoEntity> findByStatus(OrcamentoStatus status);
    List<OrcamentoEntity> findByServicoId(Long servicoId);
}
