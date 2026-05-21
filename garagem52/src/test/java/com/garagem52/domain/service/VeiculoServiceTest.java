package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.UpdateVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.VeiculoPersistenceMapper;
import com.garagem52.domain.exception.veiculo.VeiculoNotFoundException;
import com.garagem52.domain.model.Veiculo;
import com.garagem52.ports.output.VeiculoOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VeiculoService")
class VeiculoServiceTest {

    @Mock
    private VeiculoOutputPort veiculoOutputPort;

    @Mock
    private VeiculoPersistenceMapper mapper;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private VeiculoService veiculoService;

    private Veiculo veiculo;
    private VeiculoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(veiculoService, "token", "fake-token");
        veiculo = Veiculo.builder().id("v1").marca("Fiat").modelo("Uno").placa("ABC1234").ano(2020).cor("Branco").build();
        responseDTO = VeiculoResponseDTO.builder().id("v1").marca("Fiat").modelo("Uno").placa("ABC1234").build();
    }

    // ── criarVeiculo ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("criarVeiculo: retorna do cache quando placa já existe")
    void criarVeiculo_deveRetornarCache_quandoPlacaJaExiste() {
        when(veiculoOutputPort.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));
        when(mapper.toResponseDTO(veiculo)).thenReturn(responseDTO);

        VeiculoResponseDTO result = veiculoService.criarVeiculo("ABC1234");

        assertThat(result.getPlaca()).isEqualTo("ABC1234");
        verify(veiculoOutputPort, never()).save(any());
    }

    @Test
    @DisplayName("criarVeiculo: lança RuntimeException para placa inválida")
    void criarVeiculo_deveLancarException_quandoPlacaInvalida() {
        assertThatThrownBy(() -> veiculoService.criarVeiculo("INVALIDA"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("placa inválido");
    }

    @Test
    @DisplayName("criarVeiculo: busca API quando placa não está em cache e persiste")
    @SuppressWarnings("unchecked")
    void criarVeiculo_deveBuscarAPIEPersistir_quandoPlacaNaoExisteNoBanco() {
        when(veiculoOutputPort.findByPlaca("ABC1234")).thenReturn(Optional.empty());
        when(veiculoOutputPort.save(any())).thenReturn(veiculo);
        when(mapper.toResponseDTO(veiculo)).thenReturn(responseDTO);

        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), any(), any())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Veiculo.class)).thenReturn(Mono.just(veiculo));

        VeiculoResponseDTO result = veiculoService.criarVeiculo("ABC1234");

        assertThat(result).isNotNull();
        verify(veiculoOutputPort).save(any());
    }

    @Test
    @DisplayName("criarVeiculo: lança RuntimeException quando API retorna null")
    @SuppressWarnings("unchecked")
    void criarVeiculo_deveLancarException_quandoAPIRetornaNull() {
        when(veiculoOutputPort.findByPlaca("XYZ9W99")).thenReturn(Optional.empty());

        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), any(), any())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Veiculo.class)).thenReturn(Mono.empty());

        assertThatThrownBy(() -> veiculoService.criarVeiculo("XYZ9W99"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("não encontrado");
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findById: retorna DTO quando encontrado")
    void findById_deveRetornarDTO_quandoExiste() {
        when(veiculoOutputPort.findById("v1")).thenReturn(Optional.of(veiculo));
        when(mapper.toResponseDTO(veiculo)).thenReturn(responseDTO);

        VeiculoResponseDTO result = veiculoService.findById("v1");

        assertThat(result.getId()).isEqualTo("v1");
    }

    @Test
    @DisplayName("findById: lança VeiculoNotFoundException quando não encontrado")
    void findById_deveLancarException_quandoNaoExiste() {
        when(veiculoOutputPort.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> veiculoService.findById("x"))
                .isInstanceOf(VeiculoNotFoundException.class);
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll: retorna todos os veículos")
    void findAll_deveRetornarListaCompleta() {
        when(veiculoOutputPort.findAll()).thenReturn(List.of(veiculo));
        when(mapper.toResponseDTO(veiculo)).thenReturn(responseDTO);

        List<VeiculoResponseDTO> result = veiculoService.findAll();

        assertThat(result).hasSize(1);
    }

    // ── findByPlaca ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("findByPlaca: retorna DTO quando encontrado")
    void findByPlaca_deveRetornarDTO_quandoExiste() {
        when(veiculoOutputPort.findByPlaca("ABC1234")).thenReturn(Optional.of(veiculo));
        when(mapper.toResponseDTO(veiculo)).thenReturn(responseDTO);

        VeiculoResponseDTO result = veiculoService.findByPlaca("ABC1234");

        assertThat(result.getPlaca()).isEqualTo("ABC1234");
    }

    @Test
    @DisplayName("findByPlaca: lança VeiculoNotFoundException quando não encontrado")
    void findByPlaca_deveLancarException_quandoNaoExiste() {
        when(veiculoOutputPort.findByPlaca("ZZZ9999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> veiculoService.findByPlaca("ZZZ9999"))
                .isInstanceOf(VeiculoNotFoundException.class);
    }

    @Test
    @DisplayName("updateVeiculo: atualiza campos não nulos e salva")
    void updateVeiculo_deveAtualizarCamposESalvar() {
        UpdateVeiculoRequestDTO req = new UpdateVeiculoRequestDTO();
        req.setMarca("Chevrolet");
        req.setModelo("Onix");
        req.setCor("Prata");

        when(veiculoOutputPort.findById("v1")).thenReturn(Optional.of(veiculo));
        when(veiculoOutputPort.save(any())).thenReturn(veiculo);
        when(mapper.toResponseDTO(any())).thenReturn(responseDTO);

        VeiculoResponseDTO result = veiculoService.updateVeiculo("v1", req);

        assertThat(result).isNotNull();
        verify(veiculoOutputPort).save(any());
    }

    @Test
    @DisplayName("updateVeiculo: lança VeiculoNotFoundException quando não encontrado")
    void updateVeiculo_deveLancarException_quandoNaoExiste() {
        when(veiculoOutputPort.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> veiculoService.updateVeiculo("x", new UpdateVeiculoRequestDTO()))
                .isInstanceOf(VeiculoNotFoundException.class);
    }

    @Test
    @DisplayName("updateVeiculo: não altera campos nulos no request")
    void updateVeiculo_naoAlteraCamposNulos() {
        UpdateVeiculoRequestDTO req = new UpdateVeiculoRequestDTO();

        when(veiculoOutputPort.findById("v1")).thenReturn(Optional.of(veiculo));
        when(veiculoOutputPort.save(any())).thenReturn(veiculo);
        when(mapper.toResponseDTO(any())).thenReturn(responseDTO);

        veiculoService.updateVeiculo("v1", req);

        assertThat(veiculo.getMarca()).isEqualTo("Fiat");
    }

    @Test
    @DisplayName("delete: deleta quando encontrado")
    void delete_deveDeletar_quandoExiste() {
        when(veiculoOutputPort.findById("v1")).thenReturn(Optional.of(veiculo));

        veiculoService.delete("v1");

        verify(veiculoOutputPort).deleteById("v1");
    }

    @Test
    @DisplayName("delete: lança VeiculoNotFoundException quando não encontrado")
    void delete_deveLancarException_quandoNaoExiste() {
        when(veiculoOutputPort.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> veiculoService.delete("x"))
                .isInstanceOf(VeiculoNotFoundException.class);
    }


    @Test
    @DisplayName("criarVeiculo: aceita placa no formato Mercosul")
    void criarVeiculo_aceitaPlacaMercosul() {
        when(veiculoOutputPort.findByPlaca("ABC1D23")).thenReturn(Optional.of(veiculo));
        when(mapper.toResponseDTO(veiculo)).thenReturn(responseDTO);

        VeiculoResponseDTO result = veiculoService.criarVeiculo("ABC1D23");

        assertThat(result).isNotNull();
    }
}