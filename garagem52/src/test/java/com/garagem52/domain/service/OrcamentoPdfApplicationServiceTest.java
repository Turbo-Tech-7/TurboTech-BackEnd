package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.ports.input.OrcamentoInputPort;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrcamentoPdfApplicationServiceTest {

    @Mock
    private OrcamentoInputPort orcamentoInputPort;

    @Mock
    private OrcamentoPdfService pdfService;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private OrcamentoPdfApplicationService service;

    @Test
    void deveGerarPdf() {
        OrcamentoResponseDTO dto = OrcamentoResponseDTO.builder()
                .id("1")
                .build();

        when(orcamentoInputPort.findById("1"))
                .thenReturn(dto);

        when(pdfService.gerar(dto))
                .thenReturn(new byte[]{1, 2, 3});

        byte[] result = service.gerarPdf("1");

        assertEquals(3, result.length);
    }

    @Test
    void deveEnviarPdfPorEmail() {
        OrcamentoResponseDTO dto = OrcamentoResponseDTO.builder()
                .id("1")
                .nomeCliente("João")
                .emailCliente("joao@email.com")
                .descricaoServico("Troca de óleo")
                .valorTotal(150.0)
                .dataOrcamento(LocalDateTime.now())
                .veiculo(VeiculoResponseDTO.builder()
                        .placa("ABC1234")
                        .marca("VW")
                        .modelo("Gol")
                        .ano(2020)
                        .build())
                .build();

        MimeMessage mimeMessage =
                new MimeMessage(Session.getDefaultInstance(new Properties()));

        when(orcamentoInputPort.findById("1"))
                .thenReturn(dto);

        when(pdfService.gerar(dto))
                .thenReturn(new byte[]{1, 2});

        when(mailSender.createMimeMessage())
                .thenReturn(mimeMessage);

        assertDoesNotThrow(() ->
                service.enviarPdfPorEmail("1"));

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void deveLancarErroQuandoClienteNaoPossuirNome() {
        OrcamentoResponseDTO dto = OrcamentoResponseDTO.builder()
                .id("1")
                .build();

        when(orcamentoInputPort.findById("1"))
                .thenReturn(dto);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.enviarPdfPorEmail("1")
        );

        assertTrue(ex.getMessage().contains("não possui nome"));
    }
}