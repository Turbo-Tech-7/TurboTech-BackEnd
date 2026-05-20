package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.request.CreateFornecedorRequestDTO;
import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;

import java.util.List;

public interface FornecedorInputPort {
    FornecedorResponseDTO criar(CreateFornecedorRequestDTO request);
    FornecedorResponseDTO findById(String id);
    List<FornecedorResponseDTO> findAll();
    List<FornecedorResponseDTO> findByNome(String nome);
    FornecedorResponseDTO update(String id, CreateFornecedorRequestDTO request);
    void delete(String id);
}
