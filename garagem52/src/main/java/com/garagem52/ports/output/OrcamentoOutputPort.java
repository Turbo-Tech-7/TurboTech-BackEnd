package com.garagem52.ports.output;

import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;

import java.util.List;
import java.util.Optional;

public interface OrcamentoOutputPort {
    Orcamento save(Orcamento orcamento);
    Optional<Orcamento> findById(Long id);
    List<Orcamento> findAll();
    List<Orcamento> findByVeiculoId(Long veiculoId);
    List<Orcamento> findByStatus(OrcamentoStatus status);
    void deleteById(Long id);
}
