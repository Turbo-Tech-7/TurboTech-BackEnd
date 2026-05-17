package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.*;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.OrcamentoMapper;
import com.garagem52.domain.exception.orcamento.*;
import com.garagem52.domain.model.ClienteVeiculo;
import com.garagem52.domain.model.ItemOrcado;
import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.model.Servico;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import com.garagem52.ports.input.OrcamentoInputPort;
import com.garagem52.ports.output.ClienteVeiculoOutputPort;
import com.garagem52.ports.output.OrcamentoOutputPort;
import com.garagem52.ports.output.ServicoOutputPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrcamentoService implements OrcamentoInputPort {

    private final OrcamentoOutputPort orcamentoOutputPort;
    private final ServicoOutputPort servicoOutputPort;
    private final ClienteVeiculoOutputPort clienteVeiculoOutputPort;
    private final OrcamentoMapper mapper;

    @Override
    public OrcamentoResponseDTO criar(CreateOrcamentoRequestDTO request) {

        // 1. Resolve ClienteVeiculo — obrigatório para derivar veiculoId e dados do cliente
        ClienteVeiculo cv = clienteVeiculoOutputPort.findById(request.getClienteVeiculoId())
                .orElseThrow(() -> new RuntimeException(
                        "Cadastro de cliente/veículo não encontrado: " + request.getClienteVeiculoId()));

        if (cv.getVeiculoId() == null) {
            throw new RuntimeException(
                    "Veículo não vinculado ao cliente. Certifique-se de que a placa foi cadastrada.");
        }

        // 2. Cria o Servico automaticamente — mecânico não informa servicoId manualmente
        Servico servico = Servico.builder()
                .veiculoId(cv.getVeiculoId())
                .servicoOrcado("Orçamento")
                .descricaoProblema(request.getDescricaoServico())
                .dataEntrada(LocalDateTime.now())
                .status("ABERTO")
                .build();

        Servico servicoSalvo = servicoOutputPort.save(servico);

        // 3. Monta e salva o Orçamento com os IDs resolvidos internamente
        List<ItemOrcado> itens = buildItens(request.getItens());
        double totalPecas = itens.stream()
                .mapToDouble(i -> i.getValor() * i.getQuantidade()).sum();

        Orcamento o = Orcamento.builder()
                .servicoId(servicoSalvo.getId())
                .veiculoId(cv.getVeiculoId())
                .clienteVeiculoId(cv.getId())
                .valorMaoDeObra(request.getValorMaoDeObra())
                .valorTotal(totalPecas + request.getValorMaoDeObra())
                .dataOrcamento(LocalDateTime.now())
                .status(OrcamentoStatus.ABERTO)
                .descricaoServico(request.getDescricaoServico())
                // Propaga dados do cliente para campos legados (PDF, e-mail)
                .nomeCliente(cv.getNomeCliente())
                .telefoneCliente(cv.getTelefoneCliente())
                .emailCliente(cv.getEmailCliente())
                .itens(itens)
                .build();

        return mapper.toResponseDTO(orcamentoOutputPort.save(o));
    }

    @Override
    public OrcamentoResponseDTO findById(String id) {
        return mapper.toResponseDTO(orcamentoOutputPort.findById(id)
                .orElseThrow(() -> new OrcamentoNotFoundException(id)));
    }

    @Override
    public List<OrcamentoResponseDTO> findAll() {
        return orcamentoOutputPort.findAll().stream()
                .map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrcamentoResponseDTO> findByVeiculoId(String veiculoId) {
        return orcamentoOutputPort.findByVeiculoId(veiculoId).stream()
                .map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrcamentoResponseDTO> findByStatus(String status) {
        return orcamentoOutputPort.findByStatus(OrcamentoStatus.valueOf(status.toUpperCase())).stream()
                .map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public OrcamentoResponseDTO update(String id, UpdateOrcamentoRequestDTO request) {
        Orcamento existing = orcamentoOutputPort.findById(id)
                .orElseThrow(() -> new OrcamentoNotFoundException(id));

        if (request.getValorMaoDeObra() != null)   existing.setValorMaoDeObra(request.getValorMaoDeObra());
        if (request.getDescricaoServico() != null)  existing.setDescricaoServico(request.getDescricaoServico());
        if (request.getNomeCliente() != null)        existing.setNomeCliente(request.getNomeCliente());
        if (request.getTelefoneCliente() != null)    existing.setTelefoneCliente(request.getTelefoneCliente());
        if (request.getEmailCliente() != null)       existing.setEmailCliente(request.getEmailCliente());
        if (request.getItens() != null && !request.getItens().isEmpty())
            existing.setItens(buildItens(request.getItens()));
        if (request.getStatus() != null)
            aplicarTransicaoStatus(existing, request);

        double totalPecas = existing.getItens() == null ? 0 :
                existing.getItens().stream()
                        .mapToDouble(i -> i.getValor() * i.getQuantidade()).sum();
        existing.setValorTotal(totalPecas +
                (existing.getValorMaoDeObra() != null ? existing.getValorMaoDeObra() : 0));

        return mapper.toResponseDTO(orcamentoOutputPort.save(existing));
    }

    @Override
    public OrcamentoResponseDTO cancelar(String id, CancelarOrcamentoRequestDTO request) {
        if (request.getMotivo() == null) throw new MotivoCancelamentoObrigatorioException();
        Orcamento existing = orcamentoOutputPort.findById(id)
                .orElseThrow(() -> new OrcamentoNotFoundException(id));
        existing.setStatus(OrcamentoStatus.CANCELADO);
        existing.setMotivoCancelamento(request.getMotivo());
        return mapper.toResponseDTO(orcamentoOutputPort.save(existing));
    }

    @Override
    public void delete(String id) {
        orcamentoOutputPort.findById(id).orElseThrow(() -> new OrcamentoNotFoundException(id));
        orcamentoOutputPort.deleteById(id);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void aplicarTransicaoStatus(Orcamento existing, UpdateOrcamentoRequestDTO request) {
        OrcamentoStatus novoStatus = request.getStatus();
        if (novoStatus == OrcamentoStatus.CANCELADO) {
            if (request.getMotivoCancelamento() == null) throw new MotivoCancelamentoObrigatorioException();
            existing.setMotivoCancelamento(request.getMotivoCancelamento());
        } else {
            if (request.getMotivoCancelamento() != null) throw new MotivoInvalidoException();
            existing.setMotivoCancelamento(null);
        }
        existing.setStatus(novoStatus);
    }

    private List<ItemOrcado> buildItens(List<ItemOrcadoRequestDTO> dtos) {
        return dtos.stream().map(dto -> ItemOrcado.builder()
                .pecaId(dto.getPecaId())
                .nomePeca(dto.getNomePeca())
                .fornecedor(dto.getFornecedor())
                .valor(dto.getValor())
                .quantidade(dto.getQuantidade())
                .build()).collect(Collectors.toList());
    }
}
