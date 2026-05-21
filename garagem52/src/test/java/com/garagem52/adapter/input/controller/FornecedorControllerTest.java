package com.garagem52.adapter.input.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garagem52.adapter.input.dto.request.CreateFornecedorRequestDTO;
import com.garagem52.adapter.input.dto.response.FornecedorResponseDTO;
import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.FornecedorInputPort;
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

@WebMvcTest(FornecedorController.class)
@AutoConfigureMockMvc(addFilters = false)
class FornecedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FornecedorInputPort fornecedorInputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void deveCriar() throws Exception {
        Mockito.when(fornecedorInputPort.criar(any(CreateFornecedorRequestDTO.class)))
                .thenReturn(new FornecedorResponseDTO());

        CreateFornecedorRequestDTO request = new CreateFornecedorRequestDTO();
        request.setNome("Fornecedor Autopeças LTDA");

        mockMvc.perform(post("/fornecedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveAtualizar() throws Exception {
        Mockito.when(fornecedorInputPort.update(any(), any(CreateFornecedorRequestDTO.class)))
                .thenReturn(new FornecedorResponseDTO());

        CreateFornecedorRequestDTO request = new CreateFornecedorRequestDTO();
        request.setNome("Fornecedor Atualizado S/A");

        mockMvc.perform(put("/fornecedores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorId() throws Exception {

        Mockito.when(fornecedorInputPort.findById("1"))
                .thenReturn(new FornecedorResponseDTO());

        mockMvc.perform(get("/fornecedores/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveListarTodos() throws Exception {

        Mockito.when(fornecedorInputPort.findAll())
                .thenReturn(List.of(new FornecedorResponseDTO()));

        mockMvc.perform(get("/fornecedores"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorNome() throws Exception {

        Mockito.when(fornecedorInputPort.findByNome("Fornecedor"))
                .thenReturn(List.of(new FornecedorResponseDTO()));

        mockMvc.perform(get("/fornecedores/buscar-nome")
                        .param("nome", "Fornecedor"))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar() throws Exception {

        mockMvc.perform(delete("/fornecedores/1"))
                .andExpect(status().isNoContent());
    }
}