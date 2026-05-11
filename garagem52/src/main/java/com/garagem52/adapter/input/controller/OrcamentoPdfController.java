package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.doc.OrcamentoPdfControllerSwagger;
import com.garagem52.adapter.input.dto.response.MessageResponse;
import com.garagem52.ports.input.OrcamentoPdfInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor
public class OrcamentoPdfController implements OrcamentoPdfControllerSwagger {

    private final OrcamentoPdfInputPort orcamentoPdfInputPort;

    @Override
    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> gerarPdf(@PathVariable String id) {
        byte[] pdfBytes = orcamentoPdfInputPort.gerarPdf(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"orcamento-" + id + ".pdf\"")
                .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
    }

    @Override
    @PostMapping("/{id}/pdf/email")
    public ResponseEntity<MessageResponse> enviarPdfPorEmail(@PathVariable String id) {
        orcamentoPdfInputPort.enviarPdfPorEmail(id);
        return ResponseEntity.ok(new MessageResponse("Orçamento " + id + " enviado com sucesso para o e-mail do cliente."));
    }
}
