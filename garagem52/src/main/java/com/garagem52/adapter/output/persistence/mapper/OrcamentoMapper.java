package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.ClienteVeiculoResponseDTO;
import com.garagem52.adapter.input.dto.response.ItemOrcadoResponseDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.adapter.output.persistence.entity.ItemOrcadoEntity;
import com.garagem52.adapter.output.persistence.entity.OrcamentoEntity;
import com.garagem52.domain.model.ClienteVeiculo;
import com.garagem52.domain.model.ItemOrcado;
import com.garagem52.domain.model.Orcamento;
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

    @Mapping(target = "veiculo", ignore = true)
    @Mapping(target = "clienteVeiculo", ignore = true)
    Orcamento toDomain(OrcamentoEntity entity);

    OrcamentoEntity toEntity(Orcamento orcamento);

    @Mapping(target = "statusDescricao", ignore = true)
    @Mapping(target = "motivoCancelamentoDescricao", ignore = true)
    @Mapping(target = "veiculo", ignore = true)
    @Mapping(target = "clienteVeiculo", ignore = true)
    OrcamentoResponseDTO toResponseDTO(Orcamento orcamento);

    @AfterMapping
    default void preencherDescricoes(Orcamento o, @MappingTarget OrcamentoResponseDTO dto) {
        if (o.getStatus() != null)
            dto.setStatusDescricao(o.getStatus().getDescricao());
        if (o.getMotivoCancelamento() != null)
            dto.setMotivoCancelamentoDescricao(o.getMotivoCancelamento().getDescricao());
        if (o.getVeiculo() != null)
            dto.setVeiculo(new com.garagem52.adapter.input.dto.response.VeiculoResponseDTO(
                    o.getVeiculo().getId(), o.getVeiculo().getMarca(), o.getVeiculo().getModelo(),
                    o.getVeiculo().getAno(), o.getVeiculo().getPlaca(), o.getVeiculo().getCor()));
        if (o.getClienteVeiculo() != null) {
            ClienteVeiculo cv = o.getClienteVeiculo();
            dto.setClienteVeiculo(ClienteVeiculoResponseDTO.builder()
                    .id(cv.getId())
                    .nomeCliente(cv.getNomeCliente())
                    .telefoneCliente(cv.getTelefoneCliente())
                    .emailCliente(cv.getEmailCliente())
                    .placaVeiculo(cv.getPlacaVeiculo())
                    .modeloVeiculo(cv.getModeloVeiculo())
                    .veiculoId(cv.getVeiculoId())
                    .build());
        }
    }

    List<ItemOrcado> itemListToDomain(List<ItemOrcadoEntity> entities);
    List<ItemOrcadoEntity> itemListToEntity(List<ItemOrcado> items);
    List<ItemOrcadoResponseDTO> itemListToResponseDTO(List<ItemOrcado> items);
}
