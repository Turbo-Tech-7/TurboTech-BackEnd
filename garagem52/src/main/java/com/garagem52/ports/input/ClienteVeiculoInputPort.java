package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.CreateClienteVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.ClienteVeiculoResponseDTO;

import java.util.List;

public interface ClienteVeiculoInputPort {
    ClienteVeiculoResponseDTO cadastrar(CreateClienteVeiculoRequestDTO request);
    ClienteVeiculoResponseDTO findById(String id);
    List<ClienteVeiculoResponseDTO> findAll();
    List<ClienteVeiculoResponseDTO> findByNome(String nome);
    List<ClienteVeiculoResponseDTO> findByPlaca(String placa);
    void delete(String id);
}
