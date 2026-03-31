package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import com.garagem52.adapter.output.persistence.entity.FornecedorEntity;
import com.garagem52.domain.model.Fornecedor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FornecedorPersistenceMapper {

    FornecedorEntity toEntity(Fornecedor fornecedor);

    Fornecedor toDomain(FornecedorEntity entity);

    FornecedorResponseDTO toResponseDTO(Fornecedor fornecedor);
}
