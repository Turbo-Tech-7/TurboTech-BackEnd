package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.UpdateVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;

import java.util.List;

public interface VeiculoInputPort {

    VeiculoResponseDTO criarVeiculo(String Placa);

    VeiculoResponseDTO findById(Long id);

    List<VeiculoResponseDTO> findAll();

    VeiculoResponseDTO findByPlaca(String placa);

    VeiculoResponseDTO updateVeiculo(Long id, UpdateVeiculoRequestDTO requestDTO);

    void delete(Long id);
}
