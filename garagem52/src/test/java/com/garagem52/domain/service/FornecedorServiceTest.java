package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CreateFornecedorRequestDTO;
import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.FornecedorPersistenceMapper;
import com.garagem52.domain.exception.fornecedor.FornecedorNotFoundException;
import com.garagem52.domain.model.Fornecedor;
import com.garagem52.ports.output.FornecedorOutputPort;
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
@DisplayName("FornecedorService")
class FornecedorServiceTest {

    @Mock
    private FornecedorOutputPort fornecedorOutputPort;

    @Mock
    private FornecedorPersistenceMapper mapper;

    @InjectMocks
    private FornecedorService fornecedorService;

    private Fornecedor fornecedor;
    private FornecedorResponseDTO responseDTO;
    private CreateFornecedorRequestDTO request;

    @BeforeEach
    void setUp() {
        fornecedor = Fornecedor.builder().id("1").nome("Distribuidora ABC").cep("01310-100").telefone("11999999999").build();
        responseDTO = FornecedorResponseDTO.builder().id("1").nome("Distribuidora ABC").build();
        request = new CreateFornecedorRequestDTO();
        request.setNome("Distribuidora ABC");
        request.setCep("01310-100");
        request.setTelefone("11999999999");
    }

    @Test
    @DisplayName("criar: salva e retorna DTO")
    void criar_deveSalvarERetornarDTO() {
        when(fornecedorOutputPort.save(any(Fornecedor.class))).thenReturn(fornecedor);
        when(mapper.toResponseDTO(fornecedor)).thenReturn(responseDTO);

        FornecedorResponseDTO result = fornecedorService.criar(request);

        assertThat(result.getId()).isEqualTo("1");
        verify(fornecedorOutputPort).save(any(Fornecedor.class));
    }

    @Test
    @DisplayName("findById: retorna DTO quando encontrado")
    void findById_deveRetornarDTO_quandoExiste() {
        when(fornecedorOutputPort.findById("1")).thenReturn(Optional.of(fornecedor));
        when(mapper.toResponseDTO(fornecedor)).thenReturn(responseDTO);

        FornecedorResponseDTO result = fornecedorService.findById("1");

        assertThat(result.getNome()).isEqualTo("Distribuidora ABC");
    }

    @Test
    @DisplayName("findById: lança FornecedorNotFoundException quando não encontrado")
    void findById_deveLancarException_quandoNaoExiste() {
        when(fornecedorOutputPort.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fornecedorService.findById("99"))
                .isInstanceOf(FornecedorNotFoundException.class);
    }

    @Test
    @DisplayName("findAll: retorna lista de todos os fornecedores")
    void findAll_deveRetornarListaCompleta() {
        when(fornecedorOutputPort.findAll()).thenReturn(List.of(fornecedor));
        when(mapper.toResponseDTO(fornecedor)).thenReturn(responseDTO);

        List<FornecedorResponseDTO> result = fornecedorService.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("findByNome: retorna lista filtrada por nome")
    void findByNome_deveRetornarListaFiltrada() {
        when(fornecedorOutputPort.findByNome("ABC")).thenReturn(List.of(fornecedor));
        when(mapper.toResponseDTO(fornecedor)).thenReturn(responseDTO);

        List<FornecedorResponseDTO> result = fornecedorService.findByNome("ABC");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("update: atualiza campos não nulos e salva")
    void update_deveAtualizarCamposERetornarDTO() {
        when(fornecedorOutputPort.findById("1")).thenReturn(Optional.of(fornecedor));
        when(fornecedorOutputPort.save(any())).thenReturn(fornecedor);
        when(mapper.toResponseDTO(fornecedor)).thenReturn(responseDTO);

        FornecedorResponseDTO result = fornecedorService.update("1", request);

        assertThat(result).isNotNull();
        verify(fornecedorOutputPort).save(any());
    }

    @Test
    @DisplayName("update: lança FornecedorNotFoundException quando não encontrado")
    void update_deveLancarException_quandoNaoExiste() {
        when(fornecedorOutputPort.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fornecedorService.update("99", request))
                .isInstanceOf(FornecedorNotFoundException.class);
    }

    @Test
    @DisplayName("update: não altera campos nulos no request")
    void update_naoAlteraCamposNulos() {
        CreateFornecedorRequestDTO requestNulo = new CreateFornecedorRequestDTO();
        // nome, cep, telefone = null
        when(fornecedorOutputPort.findById("1")).thenReturn(Optional.of(fornecedor));
        when(fornecedorOutputPort.save(any())).thenReturn(fornecedor);
        when(mapper.toResponseDTO(fornecedor)).thenReturn(responseDTO);

        fornecedorService.update("1", requestNulo);

        assertThat(fornecedor.getNome()).isEqualTo("Distribuidora ABC");
    }

    @Test
    @DisplayName("delete: deleta quando encontrado")
    void delete_deveDeletar_quandoExiste() {
        when(fornecedorOutputPort.findById("1")).thenReturn(Optional.of(fornecedor));

        fornecedorService.delete("1");

        verify(fornecedorOutputPort).deleteById("1");
    }

    @Test
    @DisplayName("delete: lança FornecedorNotFoundException quando não encontrado")
    void delete_deveLancarException_quandoNaoExiste() {
        when(fornecedorOutputPort.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fornecedorService.delete("99"))
                .isInstanceOf(FornecedorNotFoundException.class);
    }
}
