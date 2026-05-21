package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.ItemOrcadoResponseDTO;
import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrcamentoPdfServiceTest {

    private final OrcamentoPdfService service =
            new OrcamentoPdfService();

    @Test
    void deveGerarPdf() {
        OrcamentoResponseDTO dto = OrcamentoResponseDTO.builder()
                .id("1")
                .nomeCliente("João")
                .telefoneCliente("119999999")
                .descricaoServico("Troca de óleo")
                .valorMaoDeObra(100.0)
                .valorTotal(200.0)
                .status(OrcamentoStatus.ABERTO)
                .dataOrcamento(LocalDateTime.now())
                .veiculo(VeiculoResponseDTO.builder()
                        .placa("ABC1234")
                        .marca("VW")
                        .modelo("Gol")
                        .ano(2020)
                        .build())
                .itens(List.of(
                        ItemOrcadoResponseDTO.builder()
                                .nomePeca("Filtro")
                                .fornecedor("Bosch")
                                .quantidade(1)
                                .valor(100.0)
                                .build()
                ))
                .build();

        byte[] pdf = service.gerar(dto);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }

    @Test
    void deveGerarPdfSemItens() {
        OrcamentoResponseDTO dto = OrcamentoResponseDTO.builder()
                .id("2")
                .nomeCliente("Maria")
                .status(OrcamentoStatus.FINALIZADO)
                .dataOrcamento(LocalDateTime.now())
                .build();

        byte[] pdf = service.gerar(dto);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }
}