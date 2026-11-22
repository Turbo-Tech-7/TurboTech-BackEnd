package com.garagem52.ports.output;

import com.garagem52.domain.model.Orcamento;
import com.garagem52.domain.model.Servico;

/**
 * Port de saída: o domínio só diz "preciso avisar que o status do serviço mudou".
 * Como isso vira mensagem (RabbitMQ, exchange, routing key, JSON) é problema do adapter.
 */
public interface ServicoStatusEventPublisherPort {

    void publicarStatusAtualizado(Servico servico, Orcamento orcamento);
}
