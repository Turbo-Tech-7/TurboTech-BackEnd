package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.response.PecaResponseDTO;

import java.util.List;

public interface PecaInputPort {

    List<PecaResponseDTO> findByNome(String nomePeca);

    List<PecaResponseDTO> findByValor(Double precoPeca);
}