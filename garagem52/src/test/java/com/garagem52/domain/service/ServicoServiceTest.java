package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CreateServicoRequestDTO;
import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.ServicoMapper;
import com.garagem52.domain.exception.servico.ServicoNotFoundException;
import com.garagem52.domain.model.Servico;
import com.garagem52.ports.output.ServicoOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServicoService")
class ServicoServiceTest {

    @Mock
    private ServicoOutputPort servicoOutputPort;

    @Mock
    private ServicoMapper mapper;

    @InjectMocks
    private ServicoService servicoService;

    private Servico servico;
    private ServicoResponseDTO responseDTO;
    private CreateServicoRequestDTO request;

    @BeforeEach
    void setUp() {
        servico = Servico.builder().id("s1").veiculoId("v1").servicoOrcado("Revisão").status("ABERTO").build();
        responseDTO = ServicoResponseDTO.builder().id("s1").status("ABERTO").build();
        request = new CreateServicoRequestDTO();
        request.setVeiculoId("v1");
        request.setServicoOrcado("Revisão");
        request.setDescricaoProblema("Barulho no motor");
    }

    @Test
    @DisplayName("criar: persiste serviço com status ABERTO")
    void criar_devePersisteComStatusAberto() {
        when(servicoOutputPort.save(any(Servico.class))).thenReturn(servico);
        when(mapper.toResponseDTO(servico)).thenReturn(responseDTO);

        ServicoResponseDTO result = servicoService.criar(request);

        assertThat(result.getStatus()).isEqualTo("ABERTO");
        verify(servicoOutputPort).save(any(Servico.class));
    }

    @Test
    @DisplayName("findById: retorna DTO quando encontrado")
    void findById_deveRetornarDTO_quandoExiste() {
        when(servicoOutputPort.findById("s1")).thenReturn(Optional.of(servico));
        when(mapper.toResponseDTO(servico)).thenReturn(responseDTO);

        ServicoResponseDTO result = servicoService.findById("s1");

        assertThat(result.getId()).isEqualTo("s1");
    }

    @Test
    @DisplayName("findById: lança ServicoNotFoundException quando não encontrado")
    void findById_deveLancarException_quandoNaoExiste() {
        when(servicoOutputPort.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicoService.findById("x"))
                .isInstanceOf(ServicoNotFoundException.class);
    }

    @Test
    @DisplayName("findAll: retorna todos os serviços")
    void findAll_deveRetornarListaCompleta() {
        when(servicoOutputPort.findAll()).thenReturn(List.of(servico));
        when(mapper.toResponseDTO(servico)).thenReturn(responseDTO);

        List<ServicoResponseDTO> result = servicoService.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("findByVeiculoId: retorna serviços filtrados por veiculoId")
    void findByVeiculoId_deveRetornarListaFiltrada() {
        when(servicoOutputPort.findByVeiculoId("v1")).thenReturn(List.of(servico));
        when(mapper.toResponseDTO(servico)).thenReturn(responseDTO);

        List<ServicoResponseDTO> result = servicoService.findByVeiculoId("v1");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("updateStatus: atualiza status em uppercase e salva")
    void updateStatus_deveAtualizarStatusESalvar() {
        when(servicoOutputPort.findById("s1")).thenReturn(Optional.of(servico));
        when(servicoOutputPort.save(any())).thenReturn(servico);
        when(mapper.toResponseDTO(servico)).thenReturn(responseDTO);

        servicoService.updateStatus("s1", "concluido");

        verify(servicoOutputPort).save(argThat(s -> "CONCLUIDO".equals(s.getStatus())));
    }

    @Test
    @DisplayName("updateStatus: lança ServicoNotFoundException quando não encontrado")
    void updateStatus_deveLancarException_quandoNaoExiste() {
        when(servicoOutputPort.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicoService.updateStatus("x", "CONCLUIDO"))
                .isInstanceOf(ServicoNotFoundException.class);
    }

    @Test
    @DisplayName("delete: deleta quando encontrado")
    void delete_deveDeletar_quandoExiste() {
        when(servicoOutputPort.findById("s1")).thenReturn(Optional.of(servico));

        servicoService.delete("s1");

        verify(servicoOutputPort).deleteById("s1");
    }

    @Test
    @DisplayName("delete: lança ServicoNotFoundException quando não encontrado")
    void delete_deveLancarException_quandoNaoExiste() {
        when(servicoOutputPort.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicoService.delete("x"))
                .isInstanceOf(ServicoNotFoundException.class);
    }
}