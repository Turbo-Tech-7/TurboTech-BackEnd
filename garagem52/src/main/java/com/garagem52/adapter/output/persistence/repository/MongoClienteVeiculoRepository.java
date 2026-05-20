package com.garagem52.adapter.output.persistence.repository;

import com.garagem52.adapter.output.persistence.entity.ClienteVeiculoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoClienteVeiculoRepository extends MongoRepository<ClienteVeiculoEntity, String> {

    List<ClienteVeiculoEntity> findByNomeClienteContainingIgnoreCase(String nomeCliente);

    List<ClienteVeiculoEntity> findByPlacaVeiculo(String placaVeiculo);

    Optional<ClienteVeiculoEntity> findByTelefoneCliente(String telefoneCliente);
}
