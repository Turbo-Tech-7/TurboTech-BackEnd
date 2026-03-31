package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.FornecedorEntity;
import com.garagem52.adapter.output.persistence.mapper.FornecedorPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.JpaFornecedorRepository;
import com.garagem52.domain.model.Fornecedor;
import com.garagem52.ports.output.FornecedorOutputPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FornecedorRepositoryAdapter implements FornecedorOutputPort {

    private final JpaFornecedorRepository repository;
    private final FornecedorPersistenceMapper mapper;

    @Override
    public List<Fornecedor> findByNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Fornecedor> findByCnpj(String cnpj) {
        return repository.findByCnpj(cnpj)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Fornecedor salvar(Fornecedor fornecedor) {
        FornecedorEntity entity = mapper.toEntity(fornecedor);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Fornecedor findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}