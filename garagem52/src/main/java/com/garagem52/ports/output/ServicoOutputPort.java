package com.garagem52.ports.output;

import com.garagem52.domain.model.Servico;

import java.util.List;
import java.util.Optional;

public interface ServicoOutputPort {
    Servico save(Servico servico);
    Optional<Servico> findById(Long id);
    List<Servico> findAll();
    List<Servico> findByVeiculoId(Long veiculoId);
    List<Servico> findByStatus(String status);
    void deleteById(Long id);
}
