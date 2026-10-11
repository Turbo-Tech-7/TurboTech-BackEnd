package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.UpdateVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface VeiculoInputPort {
    VeiculoResponseDTO criarVeiculo(String placa);
    VeiculoResponseDTO findById(String id);
    Page<VeiculoResponseDTO> findAll(Pageable page);
    VeiculoResponseDTO findByPlaca(String placa);
    VeiculoResponseDTO updateVeiculo(String id, UpdateVeiculoRequestDTO requestDTO);
    void delete(String id);
}
