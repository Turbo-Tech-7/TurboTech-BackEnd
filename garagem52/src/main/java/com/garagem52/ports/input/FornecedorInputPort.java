package com.garagem52.ports.input;

import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import java.util.List;

public interface FornecedorInputPort {

    List<FornecedorResponseDTO> findByNome(String nome);
    List<FornecedorResponseDTO> findByCnpj(String cnpj);

}