package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.OrcamentoEntity;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoOrcamentoRepository extends MongoRepository<OrcamentoEntity, String> {
    List<OrcamentoEntity> findByVeiculoId(String veiculoId);
    List<OrcamentoEntity> findByStatus(OrcamentoStatus status);
    List<OrcamentoEntity> findByServicoId(String servicoId);
}
