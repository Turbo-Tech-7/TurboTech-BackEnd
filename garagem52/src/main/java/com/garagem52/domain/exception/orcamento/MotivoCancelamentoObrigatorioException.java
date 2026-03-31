package com.garagem52.domain.exception.orcamento;

public class MotivoCancelamentoObrigatorioException extends RuntimeException {
    public MotivoCancelamentoObrigatorioException() {
        super("O motivo do cancelamento é obrigatório ao definir o status como CANCELADO.");
    }
}
