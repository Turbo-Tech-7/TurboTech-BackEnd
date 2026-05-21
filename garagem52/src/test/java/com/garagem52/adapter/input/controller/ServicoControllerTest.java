package com.garagem52.adapter.input.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garagem52.adapter.input.dto.request.CreateServicoRequestDTO;
import com.garagem52.adapter.input.dto.response.ServicoResponseDTO;
import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.ServicoInputPort;
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

@WebMvcTest(ServicoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServicoInputPort servicoInputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void deveCriar() throws Exception {

        Mockito.when(servicoInputPort.criar(any(CreateServicoRequestDTO.class)))
                .thenReturn(new ServicoResponseDTO());

        mockMvc.perform(post("/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateServicoRequestDTO())))
                .andExpect(status().isCreated());
    }

    @Test
    void deveBuscarPorId() throws Exception {

        Mockito.when(servicoInputPort.findById("1"))
                .thenReturn(new ServicoResponseDTO());

        mockMvc.perform(get("/servicos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveListarTodos() throws Exception {

        Mockito.when(servicoInputPort.findAll())
                .thenReturn(List.of(new ServicoResponseDTO()));

        mockMvc.perform(get("/servicos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorVeiculoId() throws Exception {

        Mockito.when(servicoInputPort.findByVeiculoId("1"))
                .thenReturn(List.of(new ServicoResponseDTO()));

        mockMvc.perform(get("/servicos/por-veiculo")
                        .param("veiculoId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveAtualizarStatus() throws Exception {

        Mockito.when(servicoInputPort.updateStatus("1", "EM_ANDAMENTO"))
                .thenReturn(new ServicoResponseDTO());

        mockMvc.perform(patch("/servicos/1/status")
                        .param("status", "EM_ANDAMENTO"))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar() throws Exception {

        mockMvc.perform(delete("/servicos/1"))
                .andExpect(status().isNoContent());
    }
}