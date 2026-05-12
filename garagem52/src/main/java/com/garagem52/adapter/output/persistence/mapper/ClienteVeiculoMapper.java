package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.output.persistence.entity.ClienteVeiculoEntity;
import com.garagem52.domain.model.ClienteVeiculo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteVeiculoMapper {
    ClienteVeiculoEntity toEntity(ClienteVeiculo domain);
    ClienteVeiculo toDomain(ClienteVeiculoEntity entity);
}
