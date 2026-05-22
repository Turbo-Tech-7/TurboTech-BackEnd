package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PecaInputPort {

    Page<PecaResponseDTO> findByNome(String nomePeca, Pageable pageable);

    List<PecaResponseDTO> findByValor(Double precoPeca);
}