package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.PecaPersistenceMapper;
import com.garagem52.domain.exception.peca.PecaNotFoundException;
import com.garagem52.domain.model.Peca;
import com.garagem52.ports.output.PecaOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PecaService")
class PecaServiceTest {

    @Mock
    private PecaOutputPort pecaOutputPort;

    @Mock
    private PecaPersistenceMapper mapper;

    @InjectMocks
    private PecaService pecaService;

    private Peca peca;
    private PecaResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        peca = Peca.builder().nome("Filtro de Óleo").descricao("Filtro").valor(45.0).build();
        responseDTO = PecaResponseDTO.builder().nomePeca("Filtro de Óleo").precoPeca(45.0).build();
    }

    @Test
    @DisplayName("findByNome: retorna lista quando peças encontradas")
    void findByNome_deveRetornarLista_quandoPecasExistem() {
        when(pecaOutputPort.findByNome("Filtro")).thenReturn(List.of(peca));
        when(mapper.toResponseDTO(peca)).thenReturn(responseDTO);

        List<PecaResponseDTO> result = pecaService.findByNome("Filtro");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNomePeca()).isEqualTo("Filtro de Óleo");
        verify(pecaOutputPort).findByNome("Filtro");
    }

    @Test
    @DisplayName("findByNome: lança PecaNotFoundException quando lista vazia")
    void findByNome_deveLancarException_quandoListaVazia() {
        when(pecaOutputPort.findByNome("Inexistente")).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> pecaService.findByNome("Inexistente"))
                .isInstanceOf(PecaNotFoundException.class)
                .hasMessageContaining("Nenhuma peça encontrada com esse nome");
    }

    @Test
    @DisplayName("findByValor: retorna lista quando peças encontradas")
    void findByValor_deveRetornarLista_quandoPecasExistem() {
        when(pecaOutputPort.findByPreco(100.0)).thenReturn(List.of(peca));
        when(mapper.toResponseDTO(peca)).thenReturn(responseDTO);

        List<PecaResponseDTO> result = pecaService.findByValor(100.0);

        assertThat(result).hasSize(1);
        verify(pecaOutputPort).findByPreco(100.0);
    }

    @Test
    @DisplayName("findByValor: lança PecaNotFoundException quando lista vazia")
    void findByValor_deveLancarException_quandoListaVazia() {
        when(pecaOutputPort.findByPreco(1.0)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> pecaService.findByValor(1.0))
                .isInstanceOf(PecaNotFoundException.class)
                .hasMessageContaining("1.0");
    }
}
