package com.garagem52.ports.output;

import com.garagem52.domain.model.Fornecedor;
import java.util.List;

public interface FornecedorOutputPort {

    List<Fornecedor> findByNome(String nome);

    List<Fornecedor> findByCnpj(String cnpj);

    Fornecedor salvar(Fornecedor fornecedor);

    Fornecedor findById(Long id);

    void deletar(Long id);
}