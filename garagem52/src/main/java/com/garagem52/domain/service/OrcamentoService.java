package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CancelarOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.CreateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.ItemOrcadoRequestDTO;
import com.garagem52.adapter.input.dto.request.UpdateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.OrcamentoMapper;
import com.garagem52.domain.exception.orcamento.MotivoCancelamentoObrigatorioException;
import com.garagem52.domain.exception.orcamento.MotivoInvalidoException;
import com.garagem52.domain.exception.orcamento.OrcamentoNotFoundException;
import com.garagem52.domain.model.ItemOrcado;
import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import com.garagem52.ports.input.OrcamentoInputPort;
import com.garagem52.ports.output.OrcamentoOutputPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrcamentoService implements OrcamentoInputPort {

    private final OrcamentoOutputPort orcamentoOutputPort;
    private final OrcamentoMapper mapper;

    @Override
    public OrcamentoResponseDTO criar(CreateOrcamentoRequestDTO request) {
        List<ItemOrcado> itens = buildItens(request.getItens());

        double totalPecas = itens.stream()
                .mapToDouble(i -> i.getValor() * i.getQuantidade()).sum();

        Orcamento orcamento = Orcamento.builder()
                .servicoId(request.getServicoId())
                .veiculoId(request.getVeiculoId())
                .valorMaoDeObra(request.getValorMaoDeObra())
                .valorTotal(totalPecas + request.getValorMaoDeObra())
                .dataOrcamento(LocalDateTime.now())
                .status(OrcamentoStatus.ABERTO)
                .nomeCliente(request.getNomeCliente())
                .telefoneCliente(request.getTelefoneCliente())
                .emailCliente(request.getEmailCliente())
                .descricaoServico(request.getDescricaoServico())
                .itens(itens)
                .build();

        return mapper.toResponseDTO(orcamentoOutputPort.save(orcamento));
    }

    @Override
    public OrcamentoResponseDTO findById(Long id) {
        return mapper.toResponseDTO(
                orcamentoOutputPort.findById(id).orElseThrow(() -> new OrcamentoNotFoundException(id)));
    }

    @Override
    public List<OrcamentoResponseDTO> findAll() {
        return orcamentoOutputPort.findAll().stream()
                .map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrcamentoResponseDTO> findByVeiculoId(Long veiculoId) {
        return orcamentoOutputPort.findByVeiculoId(veiculoId).stream()
                .map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrcamentoResponseDTO> findByStatus(String status) {
        OrcamentoStatus enumStatus = OrcamentoStatus.valueOf(status.toUpperCase());
        return orcamentoOutputPort.findByStatus(enumStatus).stream()
                .map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public OrcamentoResponseDTO update(Long id, UpdateOrcamentoRequestDTO request) {
        Orcamento existing = orcamentoOutputPort.findById(id)
                .orElseThrow(() -> new OrcamentoNotFoundException(id));

        if (request.getValorMaoDeObra() != null) existing.setValorMaoDeObra(request.getValorMaoDeObra());
        if (request.getDescricaoServico() != null) existing.setDescricaoServico(request.getDescricaoServico());
        if (request.getNomeCliente() != null) existing.setNomeCliente(request.getNomeCliente());
        if (request.getTelefoneCliente() != null) existing.setTelefoneCliente(request.getTelefoneCliente());
        if (request.getEmailCliente() != null) existing.setEmailCliente(request.getEmailCliente());
        if (request.getItens() != null && !request.getItens().isEmpty()) {
            existing.setItens(buildItens(request.getItens()));
        }

        if (request.getStatus() != null) {
            aplicarTransicaoStatus(existing, request);
        }

        double totalPecas = existing.getItens() == null ? 0 :
                existing.getItens().stream()
                        .mapToDouble(i -> i.getValor() * i.getQuantidade()).sum();
        existing.setValorTotal(totalPecas + (existing.getValorMaoDeObra() != null ? existing.getValorMaoDeObra() : 0));

        return mapper.toResponseDTO(orcamentoOutputPort.save(existing));
    }

    @Override
    public OrcamentoResponseDTO cancelar(Long id, CancelarOrcamentoRequestDTO request) {
        if (request.getMotivo() == null) {
            throw new MotivoCancelamentoObrigatorioException();
        }
        Orcamento existing = orcamentoOutputPort.findById(id)
                .orElseThrow(() -> new OrcamentoNotFoundException(id));

        existing.setStatus(OrcamentoStatus.CANCELADO);
        existing.setMotivoCancelamento(request.getMotivo());

        return mapper.toResponseDTO(orcamentoOutputPort.save(existing));
    }

    @Override
    public void delete(Long id) {
        orcamentoOutputPort.findById(id).orElseThrow(() -> new OrcamentoNotFoundException(id));
        orcamentoOutputPort.deleteById(id);
    }

    /**
     * Regras de transição de status via PUT /orcamentos/{id}:
     * - ABERTO / FINALIZADO: limpa o motivo (reabrir ou concluir não precisa de motivo)
     * - CANCELADO: exige motivo
     * - Motivo não pode ser enviado com status diferente de CANCELADO
     */
    private void aplicarTransicaoStatus(Orcamento existing, UpdateOrcamentoRequestDTO request) {
        OrcamentoStatus novoStatus = request.getStatus();

        if (novoStatus == OrcamentoStatus.CANCELADO) {
            if (request.getMotivoCancelamento() == null) {
                throw new MotivoCancelamentoObrigatorioException();
            }
            existing.setMotivoCancelamento(request.getMotivoCancelamento());
        } else {
            if (request.getMotivoCancelamento() != null) {
                throw new MotivoInvalidoException();
            }
            existing.setMotivoCancelamento(null);
        }

        existing.setStatus(novoStatus);
    }

    private List<ItemOrcado> buildItens(List<ItemOrcadoRequestDTO> dtos) {
        return dtos.stream().map(dto -> ItemOrcado.builder()
                .pecaId(dto.getPecaId())
                .fornecedor(dto.getFornecedor())
                .valor(dto.getValor())
                .quantidade(dto.getQuantidade())
                .build()
        ).collect(Collectors.toList());
    }
}
