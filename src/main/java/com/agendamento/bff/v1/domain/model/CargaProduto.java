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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "carga_produto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "carga_id", nullable = false)
    private Carga carga;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(name = "quantidade_alocada", precision = 18, scale = 6, nullable = false)
    private BigDecimal quantidadeAlocada;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @PrePersist
    protected void onCreate() {
        if (this.data == null) {
            this.data = LocalDateTime.now();
        }
    }
}
