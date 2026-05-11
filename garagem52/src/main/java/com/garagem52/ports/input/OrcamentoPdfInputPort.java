package com.garagem52.ports.input;

public interface OrcamentoPdfInputPort {
    byte[] gerarPdf(String id);
    void enviarPdfPorEmail(String id);
}
