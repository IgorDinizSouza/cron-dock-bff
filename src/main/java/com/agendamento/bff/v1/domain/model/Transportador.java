package com.agendamento.bff.v1.domain.model;

import com.agendamento.bff.v1.domain.enumeration.Status;
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
        name = "transportador",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"grupo_empresarial_id", "cnpj"})
        }
)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"grupoEmpresarial"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transportador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "descricao", length = 150, nullable = false)
    private String descricao;

    @Column(name = "cnpj", length = 20, nullable = false)
    private String cnpj;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private LocalDateTime dataAlteracao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "grupo_empresarial_id", nullable = false)
    private GrupoEmpresarial grupoEmpresarial;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.dataCriacao = now;
        this.dataAlteracao = now;
        if (this.status == null) {
            this.status = Status.ATIVO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAlteracao = LocalDateTime.now();
    }
}
