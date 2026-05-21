package com.garagem52.adapter.input.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garagem52.adapter.input.dto.request.CancelarOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.CreateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.request.ItemOrcadoRequestDTO;
import com.garagem52.adapter.input.dto.request.UpdateOrcamentoRequestDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.domain.security.JwtAuthFilter;
import com.garagem52.domain.service.JwtService;
import com.garagem52.ports.input.OrcamentoInputPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static com.garagem52.domain.utils.enums.MotivoCancelamento.CLIENTE_DESISTIU;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrcamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrcamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrcamentoInputPort orcamentoInputPort;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    void deveCriar() throws Exception {
        Mockito.when(orcamentoInputPort.criar(any(CreateOrcamentoRequestDTO.class)))
                .thenReturn(new OrcamentoResponseDTO());

        CreateOrcamentoRequestDTO request = new CreateOrcamentoRequestDTO();
        request.setClienteVeiculoId("123");
        request.setDescricaoServico("Troca de óleo e filtros");
        request.setValorMaoDeObra(150.00);
        request.setItens(List.of(new ItemOrcadoRequestDTO("123", "peca", "fornecedor", 150.00, 2)));

        mockMvc.perform(post("/orcamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveBuscarPorId() throws Exception {

        Mockito.when(orcamentoInputPort.findById("1"))
                .thenReturn(new OrcamentoResponseDTO());

        mockMvc.perform(get("/orcamentos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveListarTodos() throws Exception {

        Mockito.when(orcamentoInputPort.findAll())
                .thenReturn(List.of(new OrcamentoResponseDTO()));

        mockMvc.perform(get("/orcamentos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorVeiculoId() throws Exception {

        Mockito.when(orcamentoInputPort.findByVeiculoId("1"))
                .thenReturn(List.of(new OrcamentoResponseDTO()));

        mockMvc.perform(get("/orcamentos/por-veiculo")
                        .param("veiculoId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorStatus() throws Exception {

        Mockito.when(orcamentoInputPort.findByStatus("ABERTO"))
                .thenReturn(List.of(new OrcamentoResponseDTO()));

        mockMvc.perform(get("/orcamentos/por-status")
                        .param("status", "ABERTO"))
                .andExpect(status().isOk());
    }

    @Test
    void deveAtualizar() throws Exception {

        Mockito.when(orcamentoInputPort.update(any(), any(UpdateOrcamentoRequestDTO.class)))
                .thenReturn(new OrcamentoResponseDTO());

        mockMvc.perform(put("/orcamentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateOrcamentoRequestDTO())))
                .andExpect(status().isOk());
    }

    @Test
    void deveCancelar() throws Exception {
        Mockito.when(orcamentoInputPort.cancelar(any(), any(CancelarOrcamentoRequestDTO.class)))
                .thenReturn(new OrcamentoResponseDTO());

        CancelarOrcamentoRequestDTO request = new CancelarOrcamentoRequestDTO();
        request.setMotivo(CLIENTE_DESISTIU);

        mockMvc.perform(patch("/orcamentos/1/cancelar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletar() throws Exception {

        mockMvc.perform(delete("/orcamentos/1"))
                .andExpect(status().isNoContent());
    }
}