package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.*;
import com.garagem52.adapter.output.persistence.mapper.OrcamentoMapper;
import com.garagem52.adapter.output.persistence.repository.JpaOrcamentoRepository;
import com.garagem52.adapter.output.persistence.repository.JpaPecaRepository;
import com.garagem52.adapter.output.persistence.repository.JpaServicoRepository;
import com.garagem52.adapter.output.persistence.repository.JpaVeiculoRepository;
import com.garagem52.domain.exception.peca.PecaNotFoundException;
import com.garagem52.domain.exception.servico.ServicoNotFoundException;
import com.garagem52.domain.exception.veiculo.VeiculoNotFoundException;
import com.garagem52.domain.model.ItemOrcado;
import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import com.garagem52.ports.output.OrcamentoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrcamentoRepositoryAdapter implements OrcamentoOutputPort {

    private final JpaOrcamentoRepository jpaOrcamentoRepository;
    private final JpaServicoRepository jpaServicoRepository;
    private final JpaVeiculoRepository jpaVeiculoRepository;
    private final JpaPecaRepository jpaPecaRepository;
    private final OrcamentoMapper mapper;

    @Override
    public Orcamento save(Orcamento orcamento) {
        ServicoEntity servico = jpaServicoRepository.findById(orcamento.getServicoId())
                .orElseThrow(() -> new ServicoNotFoundException(orcamento.getServicoId()));
        VeiculoEntity veiculo = jpaVeiculoRepository.findById(orcamento.getVeiculoId())
                .orElseThrow(() -> new VeiculoNotFoundException(orcamento.getVeiculoId()));

        OrcamentoEntity entity = OrcamentoEntity.builder()
                .id(orcamento.getId())
                .servico(servico)
                .veiculo(veiculo)
                .valorMaoDeObra(orcamento.getValorMaoDeObra())
                .valorTotal(orcamento.getValorTotal())
                .dataOrcamento(orcamento.getDataOrcamento())
                .status(orcamento.getStatus())
                .motivoCancelamento(orcamento.getMotivoCancelamento())
                .nomeCliente(orcamento.getNomeCliente())
                .telefoneCliente(orcamento.getTelefoneCliente())
                .descricaoServico(orcamento.getDescricaoServico())
                .itens(new ArrayList<>())
                .build();

        OrcamentoEntity saved = jpaOrcamentoRepository.save(entity);

        if (orcamento.getItens() != null) {
            saved.getItens().clear();
            saved.getItens().addAll(buildItens(orcamento.getItens(), saved));
            saved = jpaOrcamentoRepository.save(saved);
        }

        return enrich(mapper.toDomain(saved), saved);
    }

    @Override
    public Optional<Orcamento> findById(Long id) {
        return jpaOrcamentoRepository.findById(id)
                .map(e -> enrich(mapper.toDomain(e), e));
    }

    @Override
    public List<Orcamento> findAll() {
        return jpaOrcamentoRepository.findAll().stream()
                .map(e -> enrich(mapper.toDomain(e), e)).collect(Collectors.toList());
    }

    @Override
    public List<Orcamento> findByVeiculoId(Long veiculoId) {
        return jpaOrcamentoRepository.findByVeiculoId(veiculoId).stream()
                .map(e -> enrich(mapper.toDomain(e), e)).collect(Collectors.toList());
    }

    @Override
    public List<Orcamento> findByStatus(OrcamentoStatus status) {
        return jpaOrcamentoRepository.findByStatus(status).stream()
                .map(e -> enrich(mapper.toDomain(e), e)).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaOrcamentoRepository.deleteById(id);
    }

    private List<ItemOrcadoEntity> buildItens(List<ItemOrcado> itens, OrcamentoEntity orcEntity) {
        return itens.stream().map(item -> {
            PecaEntity peca = jpaPecaRepository.findById(item.getPecaId())
                    .orElseThrow(() -> new PecaNotFoundException("Peça não encontrada: " + item.getPecaId()));
            return ItemOrcadoEntity.builder()
                    .orcamento(orcEntity)
                    .peca(peca)
                    .fornecedor(item.getFornecedor())
                    .valor(item.getValor())
                    .quantidade(item.getQuantidade())
                    .build();
        }).collect(Collectors.toList());
    }

    private Orcamento enrich(Orcamento orcamento, OrcamentoEntity entity) {
        if (entity.getVeiculo() != null) {
            orcamento.setVeiculo(com.garagem52.domain.model.Veiculo.builder()
                    .id(entity.getVeiculo().getId())
                    .marca(entity.getVeiculo().getMarca())
                    .modelo(entity.getVeiculo().getModelo())
                    .ano(entity.getVeiculo().getAno())
                    .placa(entity.getVeiculo().getPlaca())
                    .cor(entity.getVeiculo().getCor())
                    .build());
        }
        orcamento.setNomeCliente(entity.getNomeCliente());
        orcamento.setTelefoneCliente(entity.getTelefoneCliente());
        if (entity.getDescricaoServico() != null) {
            orcamento.setDescricaoServico(entity.getDescricaoServico());
        } else if (entity.getServico() != null) {
            orcamento.setDescricaoServico(entity.getServico().getDescricaoProblema());
        }
        return orcamento;
    }
}
