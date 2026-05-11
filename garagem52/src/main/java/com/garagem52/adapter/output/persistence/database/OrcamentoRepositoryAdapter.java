package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.OrcamentoMapper;
import com.garagem52.adapter.output.persistence.repository.MongoOrcamentoRepository;
import com.garagem52.adapter.output.persistence.repository.MongoVeiculoRepository;
import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.model.Veiculo;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import com.garagem52.ports.output.OrcamentoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrcamentoRepositoryAdapter implements OrcamentoOutputPort {

    private final MongoOrcamentoRepository repository;
    private final MongoVeiculoRepository veiculoRepository;
    private final OrcamentoMapper mapper;

    @Override
    public Orcamento save(Orcamento orcamento) {
        return enrich(mapper.toDomain(repository.save(mapper.toEntity(orcamento))));
    }

    @Override
    public Optional<Orcamento> findById(String id) {
        return repository.findById(id).map(e -> enrich(mapper.toDomain(e)));
    }

    @Override
    public List<Orcamento> findAll() {
        return repository.findAll().stream()
                .map(e -> enrich(mapper.toDomain(e))).collect(Collectors.toList());
    }

    @Override
    public List<Orcamento> findByVeiculoId(String veiculoId) {
        return repository.findByVeiculoId(veiculoId).stream()
                .map(e -> enrich(mapper.toDomain(e))).collect(Collectors.toList());
    }

    @Override
    public List<Orcamento> findByStatus(OrcamentoStatus status) {
        return repository.findByStatus(status).stream()
                .map(e -> enrich(mapper.toDomain(e))).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    /**
     * Substitui o FetchType.LAZY do JPA — resolve o Veiculo via lookup
     * no MongoDB e popula o campo transiente no modelo de domínio.
     */
    private Orcamento enrich(Orcamento orcamento) {
        if (orcamento.getVeiculoId() != null) {
            veiculoRepository.findById(orcamento.getVeiculoId()).ifPresent(v ->
                orcamento.setVeiculo(Veiculo.builder()
                        .id(v.getId())
                        .marca(v.getMarca())
                        .modelo(v.getModelo())
                        .ano(v.getAno())
                        .placa(v.getPlaca())
                        .cor(v.getCor())
                        .build())
            );
        }
        return orcamento;
    }
}
