package com.agendamento.bff.v1.domain.model;

import com.agendamento.bff.v1.domain.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nota_fiscal")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotaFiscal {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_empresarial_id", nullable = false)
    private GrupoEmpresarial grupoEmpresarial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "filial_id", nullable = false)
    private Filial filial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "nota_fiscal_fornecedor", nullable = false)
    private Long notaFiscalFornecedor;

    @Column(name = "qtde_itens_nota")
    private Integer qtdeItensNota;

    @Column(name = "data_nf")
    private LocalDate dataNf;

    @Column(name = "data_recebimento")
    private LocalDate dataRecebimento;

    @Column(name = "data_efetiva")
    private LocalDate dataEfetiva;

    @Column(name = "estorno", nullable = false)
    private Boolean estorno;

    @Column(name = "nota_entrada_original")
    private Long notaEntradaOriginal;

    @Column(name = "nota_fiscal_origem")
    private Long notaFiscalOrigem;

    @Column(name = "tipo_operacao")
    private Integer tipoOperacao;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NotaFiscalItem> itens = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.dataCriacao == null) this.dataCriacao = LocalDateTime.now();
        if (this.status == null) this.status = Status.ATIVO;
        if (this.estorno == null) this.estorno = false;
    }
}
