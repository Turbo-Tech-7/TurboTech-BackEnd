package com.garagem52.ports.output;

import com.garagem52.domain.model.Veiculo;

import java.util.List;
import java.util.Optional;

public interface VeiculoOutputPort {

    Veiculo save(Veiculo veiculo);

    Optional<Veiculo> findById(Long id);

    List<Veiculo> findAll();

    Optional<Veiculo> findByPlaca(String placa);

    void deleteById(Long id);

    boolean existsByPlaca(String placa);
}
