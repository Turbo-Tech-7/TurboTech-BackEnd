package com.garagem52.domain.utils.enums;

public enum MotivoCancelamento {

    CLIENTE_DESISTIU("Cliente desistiu"),
    DEMORA_NA_PECA("Demora na peça"),
    PRECO_ALTO("Preços altos"),
    OUTROS("Outros");

    private final String descricao;

    MotivoCancelamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
