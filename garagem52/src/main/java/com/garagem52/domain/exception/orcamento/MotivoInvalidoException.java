package com.garagem52.domain.exception.orcamento;

public class MotivoInvalidoException extends RuntimeException {
    public MotivoInvalidoException() {
        super("O motivo de cancelamento só pode ser informado quando o status for CANCELADO.");
    }
}
