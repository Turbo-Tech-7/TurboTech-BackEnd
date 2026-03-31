package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.response.OrcamentoResponseDTO;
import com.garagem52.ports.input.OrcamentoInputPort;
import com.garagem52.ports.input.OrcamentoPdfInputPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrcamentoPdfApplicationService implements OrcamentoPdfInputPort {

    private final OrcamentoInputPort orcamentoInputPort;
    private final OrcamentoPdfService pdfService;

    @Override
    public byte[] gerarPdf(Long orcamentoId) {
        OrcamentoResponseDTO orcamento = orcamentoInputPort.findById(orcamentoId);
        return pdfService.gerar(orcamento);
    }
}
