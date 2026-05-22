    package com.garagem52.domain.service;


    import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
    import com.garagem52.adapter.output.persistence.mapper.PecaPersistenceMapper;
    import com.garagem52.domain.exception.peca.PecaNotFoundException;
    import com.garagem52.domain.model.Peca;
    import com.garagem52.ports.input.PecaInputPort;
    import com.garagem52.ports.output.PecaOutputPort;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;

    import java.util.List;
    import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PecaService implements PecaInputPort {

        private final PecaOutputPort pecaOutputPort;
        private final PecaPersistenceMapper mapper;

    @Override
    public Page<PecaResponseDTO> findByNome(String nomePeca, Pageable pageable) {
        Page<Peca> pecas = pecaOutputPort.findByNome(nomePeca, pageable);
        if (pecas.isEmpty()) throw new PecaNotFoundException("Nenhuma peça encontrada com esse nome");
        return pecas.map(mapper::toResponseDTO);
    }

    @Override
    public List<PecaResponseDTO> findByValor(Double precoPeca) {
        List<Peca> pecas = pecaOutputPort.findByPreco(precoPeca);
        if (pecas.isEmpty()) throw new PecaNotFoundException("Nenhuma peça encontrada até o valor: " + precoPeca);
        return pecas.stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }
}
