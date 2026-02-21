package com.agendamento.bff.v1.domain.model;

import com.agendamento.bff.v1.domain.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "embalagem")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Embalagem {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_empresarial_id", nullable = false)
    private GrupoEmpresarial grupoEmpresarial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "digito")
    private Integer digito;

    @Column(name = "codigo_barra", length = 80)
    private String codigoBarra;

    @Column(name = "sigla", length = 20)
    private String sigla;

    @Column(name = "multiplicador_1", precision = 18, scale = 6)
    private BigDecimal multiplicador1;

    @Column(name = "multiplicador_2", precision = 18, scale = 6)
    private BigDecimal multiplicador2;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        if (this.dataCriacao == null) this.dataCriacao = LocalDateTime.now();
        if (this.status == null) this.status = Status.ATIVO;
    }
}