package com.garagem52.adapter.input.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garagem52.adapter.input.dto.request.CreateClienteVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.ClienteVeiculoResponseDTO;
import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.ClienteVeiculoInputPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteVeiculoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClienteVeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteVeiculoInputPort inputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("Deve cadastrar cliente veículo")
    void deveCadastrar() throws Exception {

        CreateClienteVeiculoRequestDTO request = new CreateClienteVeiculoRequestDTO();

        request.setNomeCliente("João");
        request.setTelefoneCliente("11999999999");
        request.setPlacaVeiculo("ABC1234");

        Mockito.when(inputPort.cadastrar(any(CreateClienteVeiculoRequestDTO.class)))
                .thenReturn(new ClienteVeiculoResponseDTO());

        mockMvc.perform(post("/clientes-veiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveBuscarPorId() throws Exception {

        Mockito.when(inputPort.findById("1"))
                .thenReturn(new ClienteVeiculoResponseDTO());

        mockMvc.perform(get("/clientes-veiculos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveListarTodos() throws Exception {

        Mockito.when(inputPort.findAll())
                .thenReturn(List.of(new ClienteVeiculoResponseDTO()));

        mockMvc.perform(get("/clientes-veiculos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorNome() throws Exception {

        Mockito.when(inputPort.findByNome("João"))
                .thenReturn(List.of(new ClienteVeiculoResponseDTO()));

        mockMvc.perform(get("/clientes-veiculos/buscar-nome")
                        .param("nome", "João"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorPlaca() throws Exception {

        Mockito.when(inputPort.findByPlaca("ABC1234"))
                .thenReturn(List.of(new ClienteVeiculoResponseDTO()));

        mockMvc.perform(get("/clientes-veiculos/buscar-placa")
                        .param("placa", "ABC1234"))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar() throws Exception {

        mockMvc.perform(delete("/clientes-veiculos/1"))
                .andExpect(status().isNoContent());
    }
}