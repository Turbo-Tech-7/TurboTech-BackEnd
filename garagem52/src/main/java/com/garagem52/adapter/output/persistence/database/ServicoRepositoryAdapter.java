package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.VeiculoEntity;
import com.garagem52.adapter.output.persistence.mapper.ServicoMapper;
import com.garagem52.adapter.output.persistence.repository.JpaServicoRepository;
import com.garagem52.adapter.output.persistence.repository.JpaVeiculoRepository;
import com.garagem52.domain.exception.veiculo.VeiculoNotFoundException;
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

    private final JpaServicoRepository jpaRepository;
    private final JpaVeiculoRepository jpaVeiculoRepository;
    private final ServicoMapper mapper;

    @Override
    public Servico save(Servico servico) {
        VeiculoEntity veiculo = jpaVeiculoRepository.findById(servico.getVeiculoId())
                .orElseThrow(() -> new VeiculoNotFoundException(servico.getVeiculoId()));
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(servico, veiculo)));
    }

    @Override
    public Optional<Servico> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Servico> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Servico> findByVeiculoId(Long veiculoId) {
        return jpaRepository.findByVeiculoId(veiculoId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Servico> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
