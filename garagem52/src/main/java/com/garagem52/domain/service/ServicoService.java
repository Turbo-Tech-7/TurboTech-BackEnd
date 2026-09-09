package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CreateServicoRequestDTO;
import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;
import com.garagem52.adapter.output.client.MensageriaClient;
import com.garagem52.adapter.output.client.dto.PublicarEventoRequestDTO;
import com.garagem52.adapter.output.client.dto.StatusServicoEventPayloadDTO;
import com.garagem52.adapter.output.persistence.mapper.ServicoMapper;
import com.garagem52.domain.exception.servico.ServicoNotFoundException;
import com.garagem52.domain.exception.servico.StatusServicoInvalidoException;
import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.model.Servico;
import com.garagem52.domain.utils.enums.ServicoStatus;
import com.garagem52.ports.input.ServicoInputPort;
import com.garagem52.ports.output.OrcamentoOutputPort;
import com.garagem52.ports.output.ServicoOutputPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ServicoService implements ServicoInputPort {

    private static final Logger log = LoggerFactory.getLogger(ServicoService.class);

    private final ServicoOutputPort servicoOutputPort;
    private final OrcamentoOutputPort orcamentoOutputPort;
    private final ServicoMapper mapper;
    private final MensageriaClient mensageriaClient;

    @Override
    public ServicoResponseDTO criar(CreateServicoRequestDTO request) {
        Servico s = Servico.builder()
                .servicoOrcado(request.getServicoOrcado())
                .veiculoId(request.getVeiculoId())
                .dataEntrada(LocalDateTime.now())
                .descricaoProblema(request.getDescricaoProblema())
                .status(ServicoStatus.NAO_INICIADO)
                .build();
        return mapper.toResponseDTO(servicoOutputPort.save(s));
    }

    @Override
    public ServicoResponseDTO findById(String id) {
        return mapper.toResponseDTO(servicoOutputPort.findById(id)
                .orElseThrow(() -> new ServicoNotFoundException(id)));
    }

    @Override
    public List<ServicoResponseDTO> findAll() {
        return servicoOutputPort.findAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<ServicoResponseDTO> findByVeiculoId(String veiculoId) {
        return servicoOutputPort.findByVeiculoId(veiculoId).stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public ServicoResponseDTO updateStatus(String id, String status) {
        Servico servico = servicoOutputPort.findById(id).orElseThrow(() -> new ServicoNotFoundException(id));

        ServicoStatus novoStatus = parseStatus(status);
        servico.setStatus(novoStatus);
        Servico salvo = servicoOutputPort.save(servico);

        notificarMudancaStatus(salvo, novoStatus);

        return mapper.toResponseDTO(salvo);
    }

    @Override
    public void delete(String id) {
        servicoOutputPort.findById(id).orElseThrow(() -> new ServicoNotFoundException(id));
        servicoOutputPort.deleteById(id);
    }

    private ServicoStatus parseStatus(String status) {
        try {
            return ServicoStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new StatusServicoInvalidoException(status);
        }
    }

    private void notificarMudancaStatus(Servico servico, ServicoStatus status) {
        Optional<Orcamento> orcamentoOpt = orcamentoOutputPort.findByServicoId(servico.getId()).stream().findFirst();

        if (orcamentoOpt.isEmpty()) {
            log.warn("Nenhum orçamento correlacionado ao serviço {} — notificação não enviada", servico.getId());
            return;
        }

        Orcamento orcamento = orcamentoOpt.get();
        if (orcamento.getEmailCliente() == null || orcamento.getEmailCliente().isBlank()) {
            log.warn("Orçamento {} sem e-mail de cliente — notificação não enviada", orcamento.getId());
            return;
        }

        StatusServicoEventPayloadDTO payload = StatusServicoEventPayloadDTO.builder()
                .servicoId(servico.getId())
                .orcamentoId(orcamento.getId())
                .nomeCliente(orcamento.getNomeCliente())
                .emailCliente(orcamento.getEmailCliente())
                .veiculo(montarDescricaoVeiculo(orcamento))
                .status(status.name())
                .build();

        try {
            mensageriaClient.publicar(new PublicarEventoRequestDTO("SERVICO_STATUS_ATUALIZADO", payload));
        } catch (Exception e) {
            log.error("Falha ao publicar evento de mudança de status do serviço {}", servico.getId(), e);
        }
    }

    private String montarDescricaoVeiculo(Orcamento orcamento) {
        if (orcamento.getVeiculo() == null) {
            return "";
        }
        return orcamento.getVeiculo().getModelo() + " - " + orcamento.getVeiculo().getPlaca();
    }
}
