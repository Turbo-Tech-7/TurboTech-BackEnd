package com.garagem52.adapter.input.controller;

import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.OrcamentoPdfInputPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrcamentoPdfController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrcamentoPdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrcamentoPdfInputPort orcamentoPdfInputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void deveGerarPdf() throws Exception {

        Mockito.when(orcamentoPdfInputPort.gerarPdf("1"))
                .thenReturn("pdf".getBytes());

        mockMvc.perform(get("/orcamentos/1/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    void deveEnviarPdfPorEmail() throws Exception {

        mockMvc.perform(post("/orcamentos/1/pdf/email"))
                .andExpect(status().isOk());
    }
}