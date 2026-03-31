package com.garagem52.adapter.input.controller;

import com.garagem52.adapter.input.doc.OrcamentoPdfControllerSwagger;
import com.garagem52.ports.input.OrcamentoPdfInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor
public class OrcamentoPdfController implements OrcamentoPdfControllerSwagger {

    private final OrcamentoPdfInputPort orcamentoPdfInputPort;

    @Override
    @GetMapping("/{id}/pdf")
    public ResponseEntity<InputStreamResource> gerarPdf(@PathVariable Long id) {
        byte[] pdfBytes = orcamentoPdfInputPort.gerarPdf(id);

        String filename = "orcamento-" + String.format("%04d", id) + ".pdf";

        ByteArrayInputStream bis = new ByteArrayInputStream(pdfBytes);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(new InputStreamResource(bis));
    }
}
