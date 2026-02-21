package com.agendamento.bff.v1.domain.model;

import com.agendamento.bff.v1.domain.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_empresarial_id", nullable = false)
    private GrupoEmpresarial grupoEmpresarial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprador_id")
    private Comprador comprador;

    @Column(name = "descricao", nullable = false, length = 200)
    private String descricao;

    @Column(name = "complemento", length = 200)
    private String complemento;

    @Column(name = "lastro")
    private Integer lastro;

    @Column(name = "altura", precision = 12, scale = 3)
    private java.math.BigDecimal altura;

    @Column(name = "peso", length = 30)
    private String peso;

    @Column(name = "peso_liquido", length = 30)
    private String pesoLiquido;

    @Column(name = "composicao")
    private Long composicao;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

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
