package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.ClienteVeiculoMapper;
import com.garagem52.adapter.output.persistence.mapper.OrcamentoMapper;
import com.garagem52.adapter.output.persistence.repository.MongoClienteVeiculoRepository;
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
    private final MongoClienteVeiculoRepository clienteVeiculoRepository;
    private final OrcamentoMapper mapper;
    private final ClienteVeiculoMapper clienteVeiculoMapper;

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
     * Enriquece o domínio com dados de Veiculo e ClienteVeiculo via lookup.
     * Substitui o FetchType.LAZY / @ManyToOne do JPA.
     */
    private Orcamento enrich(Orcamento o) {
        // Lookup Veiculo
        if (o.getVeiculoId() != null) {
            veiculoRepository.findById(o.getVeiculoId()).ifPresent(v ->
                o.setVeiculo(Veiculo.builder()
                        .id(v.getId())
                        .marca(v.getMarca())
                        .modelo(v.getModelo())
                        .ano(v.getAno())
                        .placa(v.getPlaca())
                        .cor(v.getCor())
                        .build())
            );
        }

        // Lookup ClienteVeiculo — preenche também os campos legados do cliente
        if (o.getClienteVeiculoId() != null) {
            clienteVeiculoRepository.findById(o.getClienteVeiculoId()).ifPresent(cv -> {
                o.setClienteVeiculo(clienteVeiculoMapper.toDomain(cv));
                // Propaga para campos legados usados no PDF e e-mail
                if (o.getNomeCliente() == null)     o.setNomeCliente(cv.getNomeCliente());
                if (o.getTelefoneCliente() == null) o.setTelefoneCliente(cv.getTelefoneCliente());
                if (o.getEmailCliente() == null)    o.setEmailCliente(cv.getEmailCliente());
            });
        }

        return o;
    }
}
