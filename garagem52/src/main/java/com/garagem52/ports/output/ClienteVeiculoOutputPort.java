package com.garagem52.ports.output;

import com.garagem52.domain.model.ClienteVeiculo;

import java.util.List;
import java.util.Optional;

public interface ClienteVeiculoOutputPort {
    ClienteVeiculo save(ClienteVeiculo clienteVeiculo);
    Optional<ClienteVeiculo> findById(String id);
    List<ClienteVeiculo> findAll();
    List<ClienteVeiculo> findByNomeCliente(String nome);
    List<ClienteVeiculo> findByPlaca(String placa);
    void deleteById(String id);
}
