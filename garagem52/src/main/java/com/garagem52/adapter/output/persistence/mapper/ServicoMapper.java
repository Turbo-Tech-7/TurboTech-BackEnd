package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;
import com.garagem52.adapter.output.persistence.entity.ServicoEntity;
import com.garagem52.adapter.output.persistence.entity.VeiculoEntity;
import com.garagem52.domain.model.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServicoMapper {

    /**
     * ServicoEntity → Servico (domínio).
     * O veiculoId vem do objeto aninhado veiculo.id.
     */
    @Mapping(source = "veiculo.id", target = "veiculoId")
    Servico toDomain(ServicoEntity entity);

    /**
     * Servico → ServicoResponseDTO.
     * Campos têm o mesmo nome, apenas veiculoId precisa ser explícito.
     */
    ServicoResponseDTO toResponseDTO(Servico servico);

    /**
     * Servico + VeiculoEntity → ServicoEntity.
     * MapStruct não consegue resolver dois parâmetros de tipos diferentes automaticamente
     * quando há ambiguidade, por isso usamos um método default para orquestrar.
     */
    default ServicoEntity toEntity(Servico servico, VeiculoEntity veiculoEntity) {
        if (servico == null) return null;
        ServicoEntity entity = new ServicoEntity();
        entity.setId(servico.getId());
        entity.setServicoOrcado(servico.getServicoOrcado());
        entity.setVeiculo(veiculoEntity);
        entity.setDataEntrada(servico.getDataEntrada());
        entity.setDescricaoProblema(servico.getDescricaoProblema());
        entity.setStatus(servico.getStatus());
        return entity;
    }
}
