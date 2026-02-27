package com.agendamento.bff.v1.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "usuario_aprovacao",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"usuario_solicitante_id"})
        }
)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"usuarioSolicitante", "usuarioAprovador", "statusAprovacaoUsuario"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAprovacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_solicitante_id", nullable = false)
    private Usuario usuarioSolicitante;

    @ManyToOne
    @JoinColumn(name = "usuario_aprovador_id")
    private Usuario usuarioAprovador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_aprovacao_usuario_id", nullable = false)
    private StatusAprovacaoUsuario statusAprovacaoUsuario;

    @Column(name = "motivo_recusa", length = 500)
    private String motivoRecusa;

    @Column(name = "data_solicitacao", nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @PrePersist
    protected void onCreate() {
        if (this.dataSolicitacao == null) {
            this.dataSolicitacao = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // timestamps are controlled by service rules for approval decisions
    }
}
