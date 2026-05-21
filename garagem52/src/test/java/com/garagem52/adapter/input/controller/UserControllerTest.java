package com.garagem52.adapter.input.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garagem52.adapter.input.dto.request.UpdateUserRequestDTO;
import com.garagem52.adapter.input.dto.response.UserResponseDTO;
import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.UserInputPort;
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

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserInputPort userInputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void deveListarTodos() throws Exception {

        Mockito.when(userInputPort.findAll())
                .thenReturn(List.of(new UserResponseDTO()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorId() throws Exception {

        Mockito.when(userInputPort.findById("1"))
                .thenReturn(new UserResponseDTO());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorEmail() throws Exception {

        Mockito.when(userInputPort.findByEmail("teste@email.com"))
                .thenReturn(new UserResponseDTO());

        mockMvc.perform(get("/users/findByEmail")
                        .param("email", "teste@email.com"))
                .andExpect(status().isOk());
    }

    @Test
    void deveAtualizar() throws Exception {

        Mockito.when(userInputPort.update(any(), any(UpdateUserRequestDTO.class)))
                .thenReturn(new UserResponseDTO());

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateUserRequestDTO())))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar() throws Exception {

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}