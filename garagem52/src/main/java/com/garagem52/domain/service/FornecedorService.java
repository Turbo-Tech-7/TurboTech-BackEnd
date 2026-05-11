package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CreateFornecedorRequestDTO;
import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.FornecedorPersistenceMapper;
import com.garagem52.domain.exception.fornecedor.FornecedorNotFoundException;
import com.garagem52.domain.model.Fornecedor;
import com.garagem52.ports.input.FornecedorInputPort;
import com.garagem52.ports.output.FornecedorOutputPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FornecedorService implements FornecedorInputPort {

    private final FornecedorOutputPort fornecedorOutputPort;
    private final FornecedorPersistenceMapper mapper;

    @Override
    public FornecedorResponseDTO criar(CreateFornecedorRequestDTO request) {
        Fornecedor f = Fornecedor.builder().nome(request.getNome()).cep(request.getCep()).telefone(request.getTelefone()).build();
        return mapper.toResponseDTO(fornecedorOutputPort.save(f));
    }

    @Override
    public FornecedorResponseDTO findById(String id) {
        return mapper.toResponseDTO(fornecedorOutputPort.findById(id)
                .orElseThrow(() -> new FornecedorNotFoundException(id)));
    }

    @Override
    public List<FornecedorResponseDTO> findAll() {
        return fornecedorOutputPort.findAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<FornecedorResponseDTO> findByNome(String nome) {
        return fornecedorOutputPort.findByNome(nome).stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public FornecedorResponseDTO update(String id, CreateFornecedorRequestDTO request) {
        Fornecedor f = fornecedorOutputPort.findById(id).orElseThrow(() -> new FornecedorNotFoundException(id));
        if (request.getNome() != null)     f.setNome(request.getNome());
        if (request.getCep() != null)      f.setCep(request.getCep());
        if (request.getTelefone() != null) f.setTelefone(request.getTelefone());
        return mapper.toResponseDTO(fornecedorOutputPort.save(f));
    }

    @Override
    public void delete(String id) {
        fornecedorOutputPort.findById(id).orElseThrow(() -> new FornecedorNotFoundException(id));
        fornecedorOutputPort.deleteById(id);
    }
}
