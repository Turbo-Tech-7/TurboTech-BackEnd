package com.garagem52.domain.exception.servico;

import com.garagem52.domain.utils.enums.ServicoStatus;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StatusServicoInvalidoException extends RuntimeException {
    public StatusServicoInvalidoException(String statusRecebido) {
        super("Status de serviço inválido: '" + statusRecebido + "'. Valores aceitos: " + valoresAceitos());
    }

    private static String valoresAceitos() {
        return Arrays.stream(ServicoStatus.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
