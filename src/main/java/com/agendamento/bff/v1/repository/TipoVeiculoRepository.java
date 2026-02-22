package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.TipoVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoVeiculoRepository extends JpaRepository<TipoVeiculo, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
