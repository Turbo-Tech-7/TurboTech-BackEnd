package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;
import com.garagem52.adapter.output.persistence.entity.ServicoEntity;
import com.garagem52.domain.model.Servico;
import org.mapstruct.Mapper;

/**
 * Mapeamento direto — veiculoId é String em ambos os lados.
 * Não há mais @ManyToOne para resolver.
 */
@Mapper(componentModel = "spring")
public interface ServicoMapper {
    Servico toDomain(ServicoEntity entity);
    ServicoResponseDTO toResponseDTO(Servico servico);
    ServicoEntity toEntity(Servico servico);
}
