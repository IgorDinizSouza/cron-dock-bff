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
@Table(name = "fornecedor")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_empresarial_id", nullable = false)
    private GrupoEmpresarial grupoEmpresarial;

    @Column(name = "cnpj", nullable = false, length = 50)
    private String cnpj;

    @Column(name = "razao_social", nullable = false, length = 200)
    private String razaoSocial;

    @Column(name = "cidade", length = 120)
    private String cidade;

    @Column(name = "uf", length = 2)
    private String uf;

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
