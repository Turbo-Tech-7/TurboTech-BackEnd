package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.ServicoMapper;
import com.garagem52.adapter.output.persistence.repository.MongoServicoRepository;
import com.garagem52.domain.model.Servico;
import com.garagem52.ports.output.ServicoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ServicoRepositoryAdapter implements ServicoOutputPort {

    private final MongoServicoRepository repository;
    private final ServicoMapper mapper;

    @Override
    public Servico save(Servico servico) {
        return mapper.toDomain(repository.save(mapper.toEntity(servico)));
    }

    @Override
    public Optional<Servico> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Servico> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Servico> findByVeiculoId(String veiculoId) {
        return repository.findByVeiculoId(veiculoId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Servico> findByStatus(String status) {
        return repository.findByStatus(status).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
