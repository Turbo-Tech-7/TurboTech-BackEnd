package com.garagem52.adapter.output.persistence.entity;

import com.garagem52.domain.utils.enums.MotivoCancelamento;
import com.garagem52.domain.utils.enums.OrcamentoStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orcamento")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class OrcamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoEntity servico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private VeiculoEntity veiculo;

    @Column(name = "valor_mao_de_obra")
    private Double valorMaoDeObra;

    @Column(name = "valor_total")
    private Double valorTotal;

    @Column(name = "data_orcamento")
    private LocalDateTime dataOrcamento;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrcamentoStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_cancelamento", length = 30)
    private MotivoCancelamento motivoCancelamento;

    @Column(name = "nome_cliente")
    private String nomeCliente;

    @Column(name = "telefone_cliente")
    private String telefoneCliente;

    @Column(name = "descricao_servico", length = 500)
    private String descricaoServico;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrcadoEntity> itens;
}