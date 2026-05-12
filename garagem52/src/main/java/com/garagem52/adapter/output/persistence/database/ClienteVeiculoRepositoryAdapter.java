package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.ClienteVeiculoMapper;
import com.garagem52.adapter.output.persistence.repository.MongoClienteVeiculoRepository;
import com.garagem52.domain.model.ClienteVeiculo;
import com.garagem52.ports.output.ClienteVeiculoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter simples — o veiculoId já chega resolvido pelo ClienteVeiculoService
 * via VeiculoInputPort.criarVeiculo(), que verifica existência e busca na API.
 */
@Component
@RequiredArgsConstructor
public class ClienteVeiculoRepositoryAdapter implements ClienteVeiculoOutputPort {

    private final MongoClienteVeiculoRepository repository;
    private final ClienteVeiculoMapper mapper;

    @Override
    public ClienteVeiculo save(ClienteVeiculo clienteVeiculo) {
        return mapper.toDomain(repository.save(mapper.toEntity(clienteVeiculo)));
    }

    @Override
    public Optional<ClienteVeiculo> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ClienteVeiculo> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<ClienteVeiculo> findByNomeCliente(String nome) {
        return repository.findByNomeClienteContainingIgnoreCase(nome).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<ClienteVeiculo> findByPlaca(String placa) {
        return repository.findByPlacaVeiculo(placa.toUpperCase()).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
