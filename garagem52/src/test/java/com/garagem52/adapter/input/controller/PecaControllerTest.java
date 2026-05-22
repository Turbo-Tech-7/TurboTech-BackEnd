package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.dto.response.PecaResponseDTO;
import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.PecaInputPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PecaController.class)
@AutoConfigureMockMvc(addFilters = false)
class PecaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PecaInputPort pecaInputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void deveBuscarPorNome() throws Exception {
        var pageResult = new PageImpl<>(List.of(new PecaResponseDTO()), PageRequest.of(0, 10), 1);

        Mockito.when(pecaInputPort.findByNome(Mockito.eq("Filtro"), Mockito.any()))
                .thenReturn(pageResult);

        mockMvc.perform(get("/pecas/buscar-nome")
                        .param("nomePeca", "Filtro"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorValor() throws Exception {
        Mockito.when(pecaInputPort.findByValor(100.0))
                .thenReturn(List.of(new PecaResponseDTO()));

        mockMvc.perform(get("/pecas/buscar-valor")
                        .param("precoPeca", "100.0"))
                .andExpect(status().isOk());
    }
}
