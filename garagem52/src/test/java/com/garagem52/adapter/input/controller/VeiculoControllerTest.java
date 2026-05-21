package com.garagem52.adapter.input.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garagem52.adapter.input.dto.request.UpdateVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.VeiculoInputPort;
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

@WebMvcTest(VeiculoController.class)
@AutoConfigureMockMvc(addFilters = false)
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VeiculoInputPort veiculoInputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void deveCriarVeiculo() throws Exception {

        Mockito.when(veiculoInputPort.criarVeiculo("ABC1234"))
                .thenReturn(new VeiculoResponseDTO());

        mockMvc.perform(post("/veiculos/criar/ABC1234"))
                .andExpect(status().isCreated());
    }

    @Test
    void deveBuscarPorId() throws Exception {

        Mockito.when(veiculoInputPort.findById("1"))
                .thenReturn(new VeiculoResponseDTO());

        mockMvc.perform(get("/veiculos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorPlaca() throws Exception {

        Mockito.when(veiculoInputPort.findByPlaca("ABC1234"))
                .thenReturn(new VeiculoResponseDTO());

        mockMvc.perform(get("/veiculos/buscar-placa")
                        .param("placa", "ABC1234"))
                .andExpect(status().isOk());
    }

    @Test
    void deveListarTodos() throws Exception {

        Mockito.when(veiculoInputPort.findAll())
                .thenReturn(List.of(new VeiculoResponseDTO()));

        mockMvc.perform(get("/veiculos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveAtualizar() throws Exception {

        Mockito.when(veiculoInputPort.updateVeiculo(any(), any(UpdateVeiculoRequestDTO.class)))
                .thenReturn(new VeiculoResponseDTO());

        mockMvc.perform(put("/veiculos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateVeiculoRequestDTO())))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar() throws Exception {

        mockMvc.perform(delete("/veiculos/1"))
                .andExpect(status().isNoContent());
    }
}