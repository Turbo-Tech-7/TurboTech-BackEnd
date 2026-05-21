package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CreateClienteVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.ClienteVeiculoResponseDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.domain.model.ClienteVeiculo;
import com.garagem52.domain.model.Veiculo;
import com.garagem52.ports.input.VeiculoInputPort;
import com.garagem52.ports.output.ClienteVeiculoOutputPort;
import com.garagem52.ports.output.VeiculoOutputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteVeiculoServiceTest {

    @Mock
    private ClienteVeiculoOutputPort outputPort;

    @Mock
    private VeiculoOutputPort veiculoOutputPort;

    @Mock
    private VeiculoInputPort veiculoInputPort;

    @InjectMocks
    private ClienteVeiculoService service;

    @Test
    void deveCadastrarClienteVeiculo() {
        CreateClienteVeiculoRequestDTO request =
                CreateClienteVeiculoRequestDTO.builder()
                        .nomeCliente("João")
                        .telefoneCliente("119999999")
                        .emailCliente("joao@email.com")
                        .placaVeiculo("abc1234")
                        .modeloVeiculo("Gol")
                        .build();

        VeiculoResponseDTO veiculoDTO = VeiculoResponseDTO.builder()
                .id("v1")
                .marca("VW")
                .modelo("Gol")
                .placa("ABC1234")
                .build();

        ClienteVeiculo salvo = ClienteVeiculo.builder()
                .id("1")
                .nomeCliente("João")
                .placaVeiculo("ABC1234")
                .modeloVeiculo("VW Gol")
                .veiculoId("v1")
                .build();

        when(veiculoInputPort.criarVeiculo("ABC1234"))
                .thenReturn(veiculoDTO);

        when(outputPort.save(any()))
                .thenReturn(salvo);

        ClienteVeiculoResponseDTO result = service.cadastrar(request);

        assertNotNull(result);
        assertEquals("João", result.getNomeCliente());
        assertEquals("ABC1234", result.getPlacaVeiculo());
    }

    @Test
    void deveBuscarPorId() {
        ClienteVeiculo cv = ClienteVeiculo.builder()
                .id("1")
                .nomeCliente("João")
                .veiculoId("v1")
                .build();

        Veiculo veiculo = Veiculo.builder()
                .id("v1")
                .marca("VW")
                .modelo("Gol")
                .build();

        when(outputPort.findById("1"))
                .thenReturn(Optional.of(cv));

        when(veiculoOutputPort.findById("v1"))
                .thenReturn(Optional.of(veiculo));

        ClienteVeiculoResponseDTO result = service.findById("1");

        assertEquals("João", result.getNomeCliente());
        assertNotNull(result.getVeiculo());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarPorId() {
        when(outputPort.findById("1"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.findById("1")
        );

        assertTrue(ex.getMessage().contains("não encontrado"));
    }

    @Test
    void deveBuscarTodos() {
        ClienteVeiculo cv = ClienteVeiculo.builder()
                .id("1")
                .nomeCliente("João")
                .build();

        when(outputPort.findAll()).thenReturn(List.of(cv));

        List<ClienteVeiculoResponseDTO> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void deveBuscarPorNome() {
        ClienteVeiculo cv = ClienteVeiculo.builder()
                .id("1")
                .nomeCliente("João")
                .build();

        when(outputPort.findByNomeCliente("João"))
                .thenReturn(List.of(cv));

        List<ClienteVeiculoResponseDTO> result =
                service.findByNome("João");

        assertEquals(1, result.size());
    }

    @Test
    void deveBuscarPorPlaca() {
        ClienteVeiculo cv = ClienteVeiculo.builder()
                .id("1")
                .placaVeiculo("ABC1234")
                .build();

        when(outputPort.findByPlaca("ABC1234"))
                .thenReturn(List.of(cv));

        List<ClienteVeiculoResponseDTO> result =
                service.findByPlaca("ABC1234");

        assertEquals(1, result.size());
    }

    @Test
    void deveDeletar() {
        ClienteVeiculo cv = ClienteVeiculo.builder()
                .id("1")
                .build();

        when(outputPort.findById("1"))
                .thenReturn(Optional.of(cv));

        service.delete("1");

        verify(outputPort).deleteById("1");
    }
}