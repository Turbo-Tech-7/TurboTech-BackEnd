package com.garagem52.ports.output;

import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrcamentoOutputPort {
    Orcamento save(Orcamento orcamento);
    Optional<Orcamento> findById(String id);
    Page<Orcamento> findAll(Pageable page);
    List<Orcamento> findByVeiculoId(String veiculoId);
    List<Orcamento> findByStatus(OrcamentoStatus status);
    List<Orcamento> findByServicoId(String servicoId);
    void deleteById(String id);
}
