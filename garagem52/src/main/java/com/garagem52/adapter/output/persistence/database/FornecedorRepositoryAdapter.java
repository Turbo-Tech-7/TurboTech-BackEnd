package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.FornecedorPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.MongoFornecedorRepository;
import com.garagem52.domain.model.Fornecedor;
import com.garagem52.ports.output.FornecedorOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FornecedorRepositoryAdapter implements FornecedorOutputPort {

    private final MongoFornecedorRepository repository;
    private final FornecedorPersistenceMapper mapper;

    @Override
    public Fornecedor save(Fornecedor fornecedor) {
        return mapper.toDomain(repository.save(mapper.toEntity(fornecedor)));
    }

    @Override
    public Optional<Fornecedor> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Fornecedor> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Fornecedor> findByNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
