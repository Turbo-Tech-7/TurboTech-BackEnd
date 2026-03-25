package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.PecaPersistenceMapper;
import com.garagem52.domain.exception.peca.PecaNotFoundException;
import com.garagem52.domain.model.Peca;
import com.garagem52.ports.input.PecaInputPort;
import com.garagem52.ports.output.PecaOutputPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PecaService implements PecaInputPort {

    private final PecaOutputPort pecaOutputPort;
    private final PecaPersistenceMapper mapper;

    @Override
    public List<PecaResponseDTO> findByNome(String nomePeca) {

        List<Peca> pecas = pecaOutputPort.findByNome(nomePeca);

        if (pecas.isEmpty()) {
            throw new PecaNotFoundException("Nenhuma peça encontrada com esse nome");
        }

        return pecas.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PecaResponseDTO> findByValor(Double precoPeca) {

        List<Peca> pecas = pecaOutputPort.findByPreco(precoPeca);

        if (pecas.isEmpty()) {
            throw new PecaNotFoundException("Nenhuma peça encontrada até o valor: " + precoPeca);
        }

        return pecas.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}