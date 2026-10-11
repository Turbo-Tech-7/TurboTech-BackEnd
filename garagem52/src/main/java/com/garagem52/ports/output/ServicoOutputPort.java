package com.garagem52.ports.output;

import com.garagem52.domain.model.Servico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ServicoOutputPort {
    Servico save(Servico servico);
    Optional<Servico> findById(String id);
    Page<Servico> findAll(Pageable page);
    List<Servico> findByVeiculoId(String veiculoId);
    List<Servico> findByStatus(String status);
    void deleteById(String id);
}
