package com.agendamento.bff.v1.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status_carga")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusCarga {

    @Id
    private Long id;

    @Column(name = "descricao", length = 100, nullable = false, unique = true)
    private String descricao;
}
