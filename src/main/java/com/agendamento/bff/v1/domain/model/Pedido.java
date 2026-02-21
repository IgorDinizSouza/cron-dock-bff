package com.agendamento.bff.v1.domain.model;

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
@Table(name = "pedido")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "id_pedido", nullable = false)
    private Long idPedido;

    @Column(name = "qtde_itens_pendentes")
    private Integer qtdeItensPendentes;

    @Column(name = "filial_regional")
    private Long filialRegional;

    @Column(name = "data_elaboracao")
    private LocalDate dataElaboracao;

    @Column(name = "tipo_operacao")
    private Integer tipoOperacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comprador_id", nullable = false)
    private Comprador comprador;

    @Column(name = "prioritario")
    private Integer prioritario;

    @Column(name = "paletizado")
    private Integer paletizado;

    @Column(name = "tipo_carga")
    private Integer tipoCarga;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PedidoItem> itens = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.dataCriacao == null) this.dataCriacao = LocalDateTime.now();
    }
}
