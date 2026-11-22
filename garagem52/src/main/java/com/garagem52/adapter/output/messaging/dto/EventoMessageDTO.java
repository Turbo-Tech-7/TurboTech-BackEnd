package com.garagem52.adapter.output.messaging.dto;

import java.time.LocalDateTime;

/**
 * Envelope da mensagem publicada no RabbitMQ. É o contrato com o microsserviço
 * mensageria (que lê esses mesmos campos em EventoMessageDTO): qualquer mudança
 * de nome/tipo aqui precisa ser espelhada lá.
 */
public record EventoMessageDTO(
        String eventoId,
        String tipo,
        LocalDateTime dataCriacao,
        Object payload
) {
}
