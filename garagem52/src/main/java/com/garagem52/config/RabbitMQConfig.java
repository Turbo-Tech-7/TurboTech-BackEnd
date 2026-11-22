package com.garagem52.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * O TurboTech é só producer: declara a exchange (idêntica à do mensageria, então a
 * declaração é idempotente) e define o JSON como formato das mensagens.
 * A fila, o binding e a DLQ continuam sendo do mensageria.
 * O Spring Boot monta o RabbitTemplate sozinho e usa o MessageConverter abaixo.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Bean
    public TopicExchange eventosExchange() {
        return new TopicExchange(exchange);
    }

    // Reaproveita o ObjectMapper do Spring Boot (JavaTimeModule já registrado,
    // datas em ISO-8601) — é o que o mensageria consegue ler no LocalDateTime.
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
