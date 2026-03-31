package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.ItemOrcadoResponseDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.adapter.output.persistence.entity.ItemOrcadoEntity;
import com.garagem52.adapter.output.persistence.entity.OrcamentoEntity;
import com.garagem52.domain.model.ItemOrcado;
import com.garagem52.domain.model.Orcamento;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrcamentoMapper {

    @Mapping(source = "orcamento.id", target = "orcamentoId")
    @Mapping(source = "peca.id", target = "pecaId")
    @Mapping(source = "peca.nomePeca", target = "nomePeca")
    @Mapping(source = "fornecedor", target = "fornecedor")
    ItemOrcado itemToDomain(ItemOrcadoEntity entity);

    ItemOrcadoResponseDTO itemToResponseDTO(ItemOrcado item);

    @Mapping(source = "servico.id", target = "servicoId")
    @Mapping(source = "veiculo.id", target = "veiculoId")
    @Mapping(source = "itens", target = "itens")
    @Mapping(source = "nomeCliente", target = "nomeCliente")
    @Mapping(source = "telefoneCliente", target = "telefoneCliente")
    @Mapping(source = "descricaoServico",target = "descricaoServico")
    @Mapping(target = "veiculo", ignore = true)
    Orcamento toDomain(OrcamentoEntity entity);

    @Mapping(target = "statusDescricao", ignore = true)
    @Mapping(target = "motivoCancelamentoDescricao", ignore = true)
    OrcamentoResponseDTO toResponseDTO(Orcamento orcamento);

    @AfterMapping
    default void preencherDescricoes(Orcamento orcamento,
                                     @MappingTarget OrcamentoResponseDTO dto) {
        if (orcamento.getStatus() != null) {
            dto.setStatusDescricao(orcamento.getStatus().getDescricao());
        }
        if (orcamento.getMotivoCancelamento() != null) {
            dto.setMotivoCancelamentoDescricao(orcamento.getMotivoCancelamento().getDescricao());
        }
    }

    List<ItemOrcado> itemListToDomain(List<ItemOrcadoEntity> entities);
    List<ItemOrcadoResponseDTO> itemListToResponseDTO(List<ItemOrcado> items);
}
