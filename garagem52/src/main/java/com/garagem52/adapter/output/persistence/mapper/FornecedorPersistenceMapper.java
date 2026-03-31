package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import com.garagem52.adapter.output.persistence.entity.FornecedorEntity;
import com.garagem52.domain.model.Fornecedor;
import org.springframework.stereotype.Component;

@Component
public class FornecedorPersistenceMapper {

    public FornecedorEntity toEntity(Fornecedor fornecedor) {
        return FornecedorEntity.builder()
                .id(fornecedor.getId())
                .nome(fornecedor.getNome())
                .cnpj(fornecedor.getCnpj())
                .telefone(fornecedor.getTelefone())
                .build();
    }

    public Fornecedor toDomain(FornecedorEntity entity) {
        return Fornecedor.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cnpj(entity.getCnpj())
                .telefone(entity.getTelefone())
                .build();
    }

    public FornecedorResponseDTO toResponseDTO(Fornecedor fornecedor) {
        return FornecedorResponseDTO.builder()
                .id(fornecedor.getId())
                .nome(fornecedor.getNome())
                .cnpj(fornecedor.getCnpj())
                .telefone(fornecedor.getTelefone())
                .build();
    }
}