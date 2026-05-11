package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.*;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;

import java.util.List;

public interface OrcamentoInputPort {
    OrcamentoResponseDTO criar(CreateOrcamentoRequestDTO request);
    OrcamentoResponseDTO findById(String id);
    List<OrcamentoResponseDTO> findAll();
    List<OrcamentoResponseDTO> findByVeiculoId(String veiculoId);
    List<OrcamentoResponseDTO> findByStatus(String status);
    OrcamentoResponseDTO update(String id, UpdateOrcamentoRequestDTO request);
    OrcamentoResponseDTO cancelar(String id, CancelarOrcamentoRequestDTO request);
    void delete(String id);
}
