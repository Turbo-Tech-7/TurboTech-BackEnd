package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.entity.PecaEntity;
import com.garagem52.adapter.output.persistence.mapper.PecaPersistenceMapper;
import com.garagem52.adapter.output.persistence.repository.MongoPecaRepository;
import com.garagem52.domain.model.Peca;
import com.garagem52.ports.output.PecaOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PecaRepositoryAdapter implements PecaOutputPort {

    private final MongoPecaRepository repository;
    private final PecaPersistenceMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Peca> findByNome(String nomePeca) {
        Pattern pattern = Pattern.compile(".*" + Pattern.quote(nomePeca) + ".*",
                Pattern.CASE_INSENSITIVE);

        Query query = new Query();
        query.addCriteria(Criteria.where("nomePeca").regex(pattern));

        return mongoTemplate.find(query, PecaEntity.class)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Peca> findByPreco(Double precoPeca) {
        return repository.findByPrecoPecaLessThanEqual(precoPeca).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Peca> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}