package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.ports.input.OrcamentoInputPort;
import com.garagem52.ports.input.OrcamentoPdfInputPort;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RequiredArgsConstructor
public class OrcamentoPdfApplicationService implements OrcamentoPdfInputPort {

    private final OrcamentoInputPort orcamentoInputPort;
    private final OrcamentoPdfService pdfService;
    private final JavaMailSender mailSender;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"));

    @Override
    public byte[] gerarPdf(Long orcamentoId) {
        OrcamentoResponseDTO orcamento = orcamentoInputPort.findById(orcamentoId);
        return pdfService.gerar(orcamento);
    }

    @Override
    public void enviarPdfPorEmail(Long orcamentoId) {
        OrcamentoResponseDTO orcamento = orcamentoInputPort.findById(orcamentoId);

        if (orcamento.getNomeCliente() == null || orcamento.getNomeCliente().isBlank()) {
            throw new RuntimeException("Orçamento #" + orcamentoId + " não possui nome do cliente.");
        }

        String destino = resolverEmailDestino(orcamento);

        byte[] pdfBytes = pdfService.gerar(orcamento);

        String nomeArquivo = "orcamento-" + String.format("%04d", orcamento.getId()) + ".pdf";
        String data  = orcamento.getDataOrcamento() != null
                ? orcamento.getDataOrcamento().format(DATE_FMT) : "—";
        String modelo = resolverModelo(orcamento);
        String total  = formatBrl(orcamento.getValorTotal());
        String placa  = orcamento.getVeiculo() != null ? orcamento.getVeiculo().getPlaca() : "—";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(destino);
            helper.setFrom("garagem.g.52@gmail.com", "Garagem52");
            helper.setSubject("Garagem52 — Orçamento #" + String.format("%04d", orcamento.getId()));
            helper.setText(
                    EmailTemplateService.orcamentoPdf(
                            orcamento.getNomeCliente(),
                            orcamento.getId(),
                            nvl(orcamento.getDescricaoServico()),
                            placa,
                            modelo,
                            total,
                            data
                    ),
                    true
            );
            helper.addAttachment(nomeArquivo, new ByteArrayDataSource(pdfBytes, "application/pdf"));
            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao enviar PDF do orçamento #" + orcamentoId + " por e-mail: " + e.getMessage(), e);
        }
    }

    private String resolverEmailDestino(OrcamentoResponseDTO orcamento) {
        String email = orcamento.getEmailCliente();
        if (email != null && !email.isBlank() && email.contains("@")) {
            return email.trim();
        }
        throw new RuntimeException(
                "E-mail do cliente não informado no orçamento #" + orcamento.getId() +
                ". Preencha o campo 'emailCliente' ao criar ou atualizar o orçamento.");
    }

    private String resolverModelo(OrcamentoResponseDTO orcamento) {
        if (orcamento.getVeiculo() == null) return "—";
        return (orcamento.getVeiculo().getMarca() + " "
                + orcamento.getVeiculo().getModelo() + " "
                + (orcamento.getVeiculo().getAno() != null ? orcamento.getVeiculo().getAno() : ""))
                .trim();
    }

    private String formatBrl(Double value) {
        if (value == null) return "R$ 0,00";
        return String.format(new Locale("pt", "BR"), "R$ %,.2f", value);
    }

    private String nvl(String s) {
        return s != null && !s.isBlank() ? s : "—";
    }
}
