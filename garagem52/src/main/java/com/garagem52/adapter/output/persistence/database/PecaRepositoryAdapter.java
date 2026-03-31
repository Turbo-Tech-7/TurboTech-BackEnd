package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.PecaPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.JpaPecaRepository;
import com.garagem52.domain.model.Peca;
import com.garagem52.ports.output.PecaOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PecaRepositoryAdapter implements PecaOutputPort {

    private final JpaPecaRepository jpaRepository;
    private final PecaPersistenceMapper mapper;

    @Override
    public List<Peca> findByNome(String nomePeca) {
        return jpaRepository.findByNomePeca(nomePeca)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Peca> findByPreco(Double precoPeca) {
        return jpaRepository.findByPrecoPeca(precoPeca)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}