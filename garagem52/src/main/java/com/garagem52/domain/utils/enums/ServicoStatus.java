package com.garagem52.domain.utils.enums;

/**
 * Status do serviço em execução na oficina. Cada transição dispara uma notificação
 * por e-mail para o cliente (ver ServicoService.notificarMudancaStatus).
 * A ordem dos valores importa: é usada para montar a linha do tempo (fases já
 * concluídas x fase atual x fases futuras) no e-mail de acompanhamento.
 */
public enum ServicoStatus {

    NAO_INICIADO("Não iniciado"),
    EM_ANDAMENTO("Em andamento"),
    SERVICO_CONCLUIDO("Serviço concluído"),
    ENTREGUE("Entregue");

    private final String descricao;

    ServicoStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /** Posição do status na linha do tempo (0 a 3) — usada para estilizar o e-mail faseado. */
    public int ordem() {
        return this.ordinal();
    }

    @Override
    public String toString() {
        return descricao;
    }
}
