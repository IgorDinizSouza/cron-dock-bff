package com.agendamento.bff.v1.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "carga")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_carga_id", nullable = false)
    private StatusCarga statusCarga;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_carga_id", nullable = false)
    private TipoCarga tipoCarga;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_veiculo_id", nullable = false)
    private TipoVeiculo tipoVeiculo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transportador_id", nullable = false)
    private Transportador transportador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "especie_carga_id", nullable = false)
    private EspecieCarga especieCarga;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_solicitante_id", nullable = false)
    private Usuario usuarioSolicitante;

    @ManyToOne
    @JoinColumn(name = "usuario_aprovador_id")
    private Usuario usuarioAprovador;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDateTime dataAgendamento;

    @PrePersist
    protected void onCreate() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
    }
}
