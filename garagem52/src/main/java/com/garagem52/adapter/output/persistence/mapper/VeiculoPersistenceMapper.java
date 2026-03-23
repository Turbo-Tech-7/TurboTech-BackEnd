package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.adapter.output.persistence.entity.VeiculoEntity;
import com.garagem52.domain.model.Veiculo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VeiculoPersistenceMapper {

    VeiculoEntity toEntity(Veiculo veiculo);

    Veiculo toDomain(VeiculoEntity veiculoEntity);

    VeiculoResponseDTO toResponseDTO(Veiculo veiculo);

}
