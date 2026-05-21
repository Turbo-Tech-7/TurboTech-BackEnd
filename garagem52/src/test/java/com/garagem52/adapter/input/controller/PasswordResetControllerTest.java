package com.garagem52.adapter.input.controller;

import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.PasswordResetInputPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PasswordResetController.class)
@AutoConfigureMockMvc(addFilters = false)
class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PasswordResetInputPort passwordResetInputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void deveSolicitarRecuperacaoSenha() throws Exception {

        Mockito.doNothing()
                .when(passwordResetInputPort)
                .solicitarRecuperacao("teste@email.com");

        mockMvc.perform(post("/auth/forgot-password")
                        .param("email", "teste@email.com"))
                .andExpect(status().isOk());
    }

    @Test
    void deveRedefinirSenha() throws Exception {

        Mockito.doNothing()
                .when(passwordResetInputPort)
                .redefinirSenha("token123", "novaSenha");

        mockMvc.perform(post("/auth/reset-password")
                        .param("token", "token123")
                        .param("novaSenha", "novaSenha"))
                .andExpect(status().isOk());
    }
}