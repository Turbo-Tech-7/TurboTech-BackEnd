package com.garagem52.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "servico")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "servico_orcado")
    private String servicoOrcado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private VeiculoEntity veiculo;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

    @Column(name = "descricao_problema")
    private String descricaoProblema;

    @Column(length = 45)
    private String status;
}
