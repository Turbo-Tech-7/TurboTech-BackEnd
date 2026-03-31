package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.CancelarOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.CreateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.UpdateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;

import java.util.List;

public interface OrcamentoInputPort {
    OrcamentoResponseDTO criar(CreateOrcamentoRequestDTO request);
    OrcamentoResponseDTO findById(Long id);
    List<OrcamentoResponseDTO> findAll();
    List<OrcamentoResponseDTO> findByVeiculoId(Long veiculoId);
    List<OrcamentoResponseDTO> findByStatus(String status);
    OrcamentoResponseDTO update(Long id, UpdateOrcamentoRequestDTO request);
    OrcamentoResponseDTO cancelar(Long id, CancelarOrcamentoRequestDTO request);
    void delete(Long id);
}
