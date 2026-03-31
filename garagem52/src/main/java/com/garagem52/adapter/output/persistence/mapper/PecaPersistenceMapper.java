package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
import com.garagem52.adapter.output.persistence.entity.PecaEntity;
import com.garagem52.domain.model.Peca;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PecaPersistenceMapper {

    @Mapping(source = "nome",     target = "nomePeca")
    @Mapping(source = "descricao", target = "descricaoPeca")
    @Mapping(source = "valor",    target = "precoPeca")
    PecaEntity toEntity(Peca peca);

    @Mapping(source = "nomePeca",    target = "nome")
    @Mapping(source = "descricaoPeca", target = "descricao")
    @Mapping(source = "precoPeca",   target = "valor")
    Peca toDomain(PecaEntity entity);

    @Mapping(source = "id",       target = "idPeca")
    @Mapping(source = "nome",     target = "nomePeca")
    @Mapping(source = "descricao", target = "descricaoPeca")
    @Mapping(source = "valor",    target = "precoPeca")
    PecaResponseDTO toResponseDTO(Peca peca);
}
