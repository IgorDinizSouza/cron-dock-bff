package com.agendamento.bff.v1.domain.model;

import com.agendamento.bff.v1.domain.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "filial")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filial {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_empresarial_id", nullable = false)
    private GrupoEmpresarial grupoEmpresarial;

    @Column(name = "descricao", nullable = false, length = 120)
    private String descricao;

    @Column(name = "cnpj", nullable = false, length = 50)
    private String cnpj;

    @Column(name = "endereco", length = 255)
    private String endereco;

    @Column(name = "bairro", length = 120)
    private String bairro;

    @Column(name = "codigo_ibge_cidade", length = 20)
    private String codigoIbgeCidade;

    @Column(name = "uf", length = 2)
    private String uf;

    @Column(name = "cep", length = 20)
    private String cep;

    @Column(name = "cd", nullable = false)
    private Integer cd;

    @Column(name = "wms", nullable = false)
    private Integer wms;

    @Column(name = "flag_regional")
    private Integer flagRegional;

    @Column(name = "descricao_regional", length = 200)
    private String descricaoRegional;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        if (this.dataCriacao == null) this.dataCriacao = LocalDateTime.now();
        if (this.status == null) this.status = Status.ATIVO;
        if (this.cd == null) this.cd = 0;
        if (this.wms == null) this.wms = 0;
    }
}