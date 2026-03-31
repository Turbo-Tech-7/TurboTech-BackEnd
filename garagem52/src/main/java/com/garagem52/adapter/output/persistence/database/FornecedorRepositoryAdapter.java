package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.FornecedorPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.JpaFornecedorRepository;
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

    private final JpaFornecedorRepository jpaRepository;
    private final FornecedorPersistenceMapper mapper;

    @Override
    public Fornecedor save(Fornecedor fornecedor) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(fornecedor)));
    }

    @Override
    public Optional<Fornecedor> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Fornecedor> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Fornecedor> findByNome(String nome) {
        return jpaRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
