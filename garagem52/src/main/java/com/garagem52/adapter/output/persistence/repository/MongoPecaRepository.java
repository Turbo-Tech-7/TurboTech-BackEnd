package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.PecaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoPecaRepository extends MongoRepository<PecaEntity, String> {
    Page<PecaEntity> findByNomePecaContainingIgnoreCase(String nomePeca, Pageable pageable);
    List<PecaEntity> findByPrecoPecaLessThanEqual(Double precoPeca);
}
