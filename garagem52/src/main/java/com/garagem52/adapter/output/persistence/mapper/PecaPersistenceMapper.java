package com.garagem52.adapter.output.persistence.mapper;

import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
import com.garagem52.adapter.output.persistence.entity.PecaEntity;
import com.garagem52.domain.model.Peca;
import org.springframework.stereotype.Component;

@Component
public class PecaPersistenceMapper {

    public PecaEntity toEntity(Peca peca) {
        return PecaEntity.builder()
                .id(peca.getId())
                .nomePeca(peca.getNome())
                .descricaoPeca(peca.getDescricao())
                .precoPeca(peca.getValor())
                .build();
    }

    public Peca toDomain(PecaEntity entity) {
        return Peca.builder()
                .id(entity.getId())
                .nome(entity.getNomePeca())
                .descricao(entity.getDescricaoPeca())
                .valor(entity.getPrecoPeca())
                .build();
    }

    public PecaResponseDTO toResponseDTO(Peca peca) {
        return PecaResponseDTO.builder()
                .idPeca(peca.getId())
                .nomePeca(peca.getNome())
                .descricaoPeca(peca.getDescricao())
                .precoPeca(peca.getValor())
                .build();
    }
}