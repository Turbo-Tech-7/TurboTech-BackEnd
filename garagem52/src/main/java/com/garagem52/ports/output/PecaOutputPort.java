package com.garagem52.ports.output;

import com.garagem52.domain.model.Peca;

import java.util.List;

public interface PecaOutputPort {

    List<Peca> findByNome(String nomePeca);

    List<Peca> findByPreco(Double precoPeca);
}