package com.garagem52.ports.output;

import com.garagem52.domain.model.Fornecedor;

import java.util.List;
import java.util.Optional;

public interface FornecedorOutputPort {
    Fornecedor save(Fornecedor fornecedor);
    Optional<Fornecedor> findById(Long id);
    List<Fornecedor> findAll();
    List<Fornecedor> findByNome(String nome);
    void deleteById(Long id);
}
