package com.agendamento.bff.v1.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pedido_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "filial_id", nullable = false)
    private Filial filial;

    @Column(name = "sequencia", nullable = false)
    private Integer sequencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "embalagem_id", nullable = false)
    private Embalagem embalagem;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @Column(name = "qtd_pedida", precision = 18, scale = 6)
    private BigDecimal qtdPedida;

    @Column(name = "qtd_recebida", precision = 18, scale = 6)
    private BigDecimal qtdRecebida;

    @Column(name = "carga_palet")
    private Integer cargaPalet;

    @Column(name = "abc")
    private Integer abc;

    @Column(name = "participacao_item", precision = 18, scale = 6)
    private BigDecimal participacaoItem;
}
