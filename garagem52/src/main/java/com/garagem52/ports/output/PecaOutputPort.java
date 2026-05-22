package com.garagem52.ports.output;

import com.garagem52.domain.model.Peca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PecaOutputPort {
    Page<Peca> findByNome(String nomePeca, Pageable pageable);
    List<Peca> findByPreco(Double precoPeca);
    Optional<Peca> findById(String id);
}
