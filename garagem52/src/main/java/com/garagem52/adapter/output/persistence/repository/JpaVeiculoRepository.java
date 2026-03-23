package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaVeiculoRepository extends JpaRepository<VeiculoEntity, Long> {

    Optional<VeiculoEntity> findByPlaca(String placa);

    boolean existsByPlaca(String placa);

}
