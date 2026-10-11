package com.garagem52.ports.output;

import com.garagem52.domain.model.Veiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VeiculoOutputPort {
    Veiculo save(Veiculo veiculo);
    Optional<Veiculo> findById(String id);
    Page<Veiculo> findAll(Pageable page);
    Optional<Veiculo> findByPlaca(String placa);
    void deleteById(String id);
    boolean existsByPlaca(String placa);
}
