package com.garagem52.adapter.output.messaging;

import com.garagem52.adapter.output.messaging.dto.EventoMessageDTO;
import com.garagem52.adapter.output.messaging.dto.StatusServicoEventPayloadDTO;
import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.model.Servico;
import com.garagem52.ports.output.ServicoStatusEventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Adapter de saída (producer): publica o evento diretamente na exchange do RabbitMQ.
 * O microsserviço mensageria só consome — a fila e a DLQ são declaradas por ele.
 */
@Component
@RequiredArgsConstructor
public class RabbitServicoStatusEventPublisher implements ServicoStatusEventPublisherPort {

    static final String TIPO_EVENTO = "SERVICO_STATUS_ATUALIZADO";

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    @Override
    public void publicarStatusAtualizado(Servico servico, Orcamento orcamento) {
        StatusServicoEventPayloadDTO payload = StatusServicoEventPayloadDTO.builder()
                .servicoId(servico.getId())
                .orcamentoId(orcamento.getId())
                .nomeCliente(orcamento.getNomeCliente())
                .emailCliente(orcamento.getEmailCliente())
                .veiculo(descreverVeiculo(orcamento))
                .status(servico.getStatus().name())
                .build();

        EventoMessageDTO evento = new EventoMessageDTO(
                UUID.randomUUID().toString(),
                TIPO_EVENTO,
                LocalDateTime.now(),
                payload
        );

        rabbitTemplate.convertAndSend(exchange, routingKey, evento);
    }

    private String descreverVeiculo(Orcamento orcamento) {
        if (orcamento.getVeiculo() == null) {
            return "";
        }
        return orcamento.getVeiculo().getModelo() + " - " + orcamento.getVeiculo().getPlaca();
    }
}
