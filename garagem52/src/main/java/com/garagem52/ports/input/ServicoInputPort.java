package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.CreateServicoRequestDTO;
import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServicoInputPort {
    ServicoResponseDTO criar(CreateServicoRequestDTO request);
    ServicoResponseDTO findById(String id);
    Page<ServicoResponseDTO> findAll(Pageable page);
    List<ServicoResponseDTO> findByVeiculoId(String veiculoId);
    ServicoResponseDTO updateStatus(String id, String status);
    void delete(String id);
}
