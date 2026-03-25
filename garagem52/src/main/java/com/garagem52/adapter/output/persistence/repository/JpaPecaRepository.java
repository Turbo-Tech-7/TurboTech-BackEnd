package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.PecaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPecaRepository extends JpaRepository<PecaEntity, Long> {

    List<PecaEntity> findByNomePeca(String nomePeca);

    List<PecaEntity> findByPrecoPeca(Double precoPeca);
}