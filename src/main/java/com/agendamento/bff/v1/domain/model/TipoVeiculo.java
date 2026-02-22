package com.agendamento.bff.v1.domain.model;

import com.agendamento.bff.v1.domain.enumeration.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_veiculo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoVeiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 30, nullable = false)
    private String nome;

    @Column(name = "quantidade_maxima_paletes")
    private Integer quantidadeMaximaPaletes;

    @Column(name = "status", nullable = false)
    private Status status;

    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = Status.ATIVO;
        }
    }
}
