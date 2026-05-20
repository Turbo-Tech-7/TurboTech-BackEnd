package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.CreateServicoRequestDTO;
import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;

import java.util.List;

public interface ServicoInputPort {
    ServicoResponseDTO criar(CreateServicoRequestDTO request);
    ServicoResponseDTO findById(String id);
    List<ServicoResponseDTO> findAll();
    List<ServicoResponseDTO> findByVeiculoId(String veiculoId);
    ServicoResponseDTO updateStatus(String id, String status);
    void delete(String id);
}
