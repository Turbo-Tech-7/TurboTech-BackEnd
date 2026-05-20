package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.VeiculoPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.MongoVeiculoRepository;
import com.garagem52.domain.model.Veiculo;
import com.garagem52.ports.output.VeiculoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VeiculoRepositoryAdapter implements VeiculoOutputPort {

    private final MongoVeiculoRepository repository;
    private final VeiculoPersistenceMapper mapper;

    @Override
    public Veiculo save(Veiculo veiculo) {
        return mapper.toDomain(repository.save(mapper.toEntity(veiculo)));
    }

    @Override
    public Optional<Veiculo> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Veiculo> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Veiculo> findByPlaca(String placa) {
        return repository.findByPlaca(placa).map(mapper::toDomain);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByPlaca(String placa) {
        return repository.existsByPlaca(placa);
    }
}
