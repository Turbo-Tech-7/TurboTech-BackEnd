package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.CreateServicoRequestDTO;
import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;

import java.util.List;

public interface ServicoInputPort {
    ServicoResponseDTO criar(CreateServicoRequestDTO request);
    ServicoResponseDTO findById(Long id);
    List<ServicoResponseDTO> findAll();
    List<ServicoResponseDTO> findByVeiculoId(Long veiculoId);
    ServicoResponseDTO updateStatus(Long id, String status);
    void delete(Long id);
}
