package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CreateServicoRequestDTO;
import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.ServicoMapper;
import com.garagem52.domain.exception.servico.ServicoNotFoundException;
import com.garagem52.domain.model.Servico;
import com.garagem52.ports.input.ServicoInputPort;
import com.garagem52.ports.output.ServicoOutputPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ServicoService implements ServicoInputPort {

    private final ServicoOutputPort servicoOutputPort;
    private final ServicoMapper mapper;

    @Override
    public ServicoResponseDTO criar(CreateServicoRequestDTO request) {
        Servico s = Servico.builder()
                .servicoOrcado(request.getServicoOrcado())
                .veiculoId(request.getVeiculoId())
                .dataEntrada(LocalDateTime.now())
                .descricaoProblema(request.getDescricaoProblema())
                .status("ABERTO")
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
        Servico s = servicoOutputPort.findById(id).orElseThrow(() -> new ServicoNotFoundException(id));
        s.setStatus(status.toUpperCase());
        return mapper.toResponseDTO(servicoOutputPort.save(s));
    }

    @Override
    public void delete(String id) {
        servicoOutputPort.findById(id).orElseThrow(() -> new ServicoNotFoundException(id));
        servicoOutputPort.deleteById(id);
    }
}
