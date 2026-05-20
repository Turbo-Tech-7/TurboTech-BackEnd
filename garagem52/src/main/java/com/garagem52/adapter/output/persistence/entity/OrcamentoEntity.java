package com.garagem52.adapter.output.persistence.entity;

import com.garagem52.domain.utils.enums.MotivoCancelamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orcamento")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class OrcamentoEntity {

    @Id
    private String id;

    @Indexed
    @Field("servico_id")
    private String servicoId;

    @Indexed
    @Field("veiculo_id")
    private String veiculoId;

    /**
     * Referência ao registro de entrada do cliente (collection cliente_veiculo).
     * Substitui os campos soltos nomeCliente/telefoneCliente/emailCliente
     * que eram duplicados — agora lidos diretamente do ClienteVeiculo.
     * Mantidos abaixo como fallback para orçamentos antigos ou criações manuais.
     */
    @Indexed
    @Field("cliente_veiculo_id")
    private String clienteVeiculoId;

    @Field("valor_mao_de_obra")
    private Double valorMaoDeObra;

    @Field("valor_total")
    private Double valorTotal;

    @Indexed
    @Field("data_orcamento")
    private LocalDateTime dataOrcamento;

    @Indexed
    private OrcamentoStatus status;

    @Field("motivo_cancelamento")
    private MotivoCancelamento motivoCancelamento;

    // Mantidos como fallback / compatibilidade
    @Field("nome_cliente")
    private String nomeCliente;

    @Field("telefone_cliente")
    private String telefoneCliente;

    @Field("email_cliente")
    private String emailCliente;

    @Field("descricao_servico")
    private String descricaoServico;

    // Array embedded — itens do orçamento
    private List<ItemOrcadoEntity> itens;
}
