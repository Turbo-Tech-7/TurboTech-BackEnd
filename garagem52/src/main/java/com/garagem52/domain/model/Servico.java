package com.garagem52.domain.model;

import com.garagem52.domain.utils.enums.ServicoStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;          // ObjectId do MongoDB
    private String servicoOrcado;
    private String veiculoId;   // referência por ObjectId
    private LocalDateTime dataEntrada;
    private String descricaoProblema;
    private ServicoStatus status;
}
