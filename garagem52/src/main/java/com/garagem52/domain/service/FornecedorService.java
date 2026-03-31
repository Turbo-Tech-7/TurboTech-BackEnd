package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.FornecedorRequestDTO;
import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.FornecedorPersistenceMapper;
import com.garagem52.domain.model.Fornecedor;
import com.garagem52.ports.input.FornecedorInputPort;
import com.garagem52.ports.output.FornecedorOutputPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FornecedorService implements FornecedorInputPort {

    private final FornecedorOutputPort fornecedorOutputPort;
    private final FornecedorPersistenceMapper mapper;

    @Override
    public List<FornecedorResponseDTO> findByNome(String nome) {
        return fornecedorOutputPort.findByNome(nome)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FornecedorResponseDTO> findByCnpj(String cnpj) {
        return fornecedorOutputPort.findByCnpj(cnpj)
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FornecedorResponseDTO criar(FornecedorRequestDTO dto) {
        Fornecedor fornecedor = Fornecedor.builder()
                .nome(dto.getNome())
                .cnpj(dto.getCnpj())
                .telefone(dto.getTelefone())
                .build();

        return mapper.toResponseDTO(fornecedorOutputPort.salvar(fornecedor));
    }

    @Override
    public FornecedorResponseDTO atualizar(Long id, FornecedorRequestDTO dto) {
        Fornecedor fornecedor = fornecedorOutputPort.findById(id);

        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setTelefone(dto.getTelefone());

        return mapper.toResponseDTO(fornecedorOutputPort.salvar(fornecedor));
    }

    @Override
    public void deletar(Long id) {
        fornecedorOutputPort.deletar(id);
    }

    @Override
    public FornecedorResponseDTO buscarPorId(Long id) {
        return mapper.toResponseDTO(fornecedorOutputPort.findById(id));
    }
}