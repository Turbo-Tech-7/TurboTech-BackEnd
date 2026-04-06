package com.garagem52.ports.input;

public interface OrcamentoPdfInputPort {
    byte[] gerarPdf(Long orcamentoId);
    void enviarPdfPorEmail(Long orcamentoId);
}
