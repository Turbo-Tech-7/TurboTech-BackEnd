package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.ClienteVeiculoResponseDTO;
import com.garagem52.adapter.input.dto.response.ItemOrcadoResponseDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO; // Importe o seu VeiculoResponseDTO
import com.garagem52.adapter.output.persistence.entity.ItemOrcadoEntity;
import com.garagem52.adapter.output.persistence.entity.OrcamentoEntity;
import com.garagem52.domain.model.ClienteVeiculo;
import com.garagem52.domain.model.ItemOrcado;
import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.model.Veiculo; // Importe o seu Veiculo do domínio
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrcamentoMapper {

    ItemOrcado itemToDomain(ItemOrcadoEntity entity);
    ItemOrcadoEntity itemToEntity(ItemOrcado item);
    ItemOrcadoResponseDTO itemToResponseDTO(ItemOrcado item);

    // Mappers auxiliares para o MapStruct saber como converter os objetos internos automaticamente
    VeiculoResponseDTO veiculoToResponseDTO(Veiculo veiculo);
    ClienteVeiculoResponseDTO clienteVeiculoToResponseDTO(ClienteVeiculo clienteVeiculo);

    @Mapping(target = "veiculo", ignore = true)
    @Mapping(target = "clienteVeiculo", ignore = true)
    Orcamento toDomain(OrcamentoEntity entity);

    OrcamentoEntity toEntity(Orcamento orcamento);

    // REMOVIDOS os ignores de 'veiculo' e 'clienteVeiculo'
    @Mapping(target = "statusDescricao", ignore = true)
    @Mapping(target = "motivoCancelamentoDescricao", ignore = true)
    OrcamentoResponseDTO toResponseDTO(Orcamento orcamento);

    @AfterMapping
    default void preencherDescricoes(Orcamento o, @MappingTarget OrcamentoResponseDTO dto) {
        if (o.getStatus() != null) {
            dto.setStatusDescricao(o.getStatus().getDescricao());
        }
        if (o.getMotivoCancelamento() != null) {
            dto.setMotivoCancelamentoDescricao(o.getMotivoCancelamento().getDescricao());
        }
        // Não precisa mais setar Veiculo e ClienteVeiculo manualmente aqui! O MapStruct faz sozinho lá em cima.
    }

    List<ItemOrcado> itemListToDomain(List<ItemOrcadoEntity> entities);
    List<ItemOrcadoEntity> itemListToEntity(List<ItemOrcado> items);
    List<ItemOrcadoResponseDTO> itemListToResponseDTO(List<ItemOrcado> items);
}