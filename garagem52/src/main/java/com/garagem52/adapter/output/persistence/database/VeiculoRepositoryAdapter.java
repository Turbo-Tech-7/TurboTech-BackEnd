package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.VeiculoEntity;
import com.garagem52.adapter.output.persistence.mapper.VeiculoPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.JpaVeiculoRepository;
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

    private final JpaVeiculoRepository jpaRepository;
    private final VeiculoPersistenceMapper mapper;

    @Override
    public Veiculo save(Veiculo veiculo) {
        VeiculoEntity entity = jpaRepository.save(mapper.toEntity(veiculo));
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Veiculo> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Veiculo> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Veiculo> findByPlaca(String placa) {
        return jpaRepository.findByPlaca(placa)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByPlaca(String placa) {
        return jpaRepository.existsByPlaca(placa);
    }
}

