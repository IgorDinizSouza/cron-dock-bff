package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.TempoDescarregamentoPalete;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TempoDescarregamentoPaleteRepository extends JpaRepository<TempoDescarregamentoPalete, Long> {

    @Override
    @EntityGraph(attributePaths = {"tipoVeiculo", "tipoCarga"})
    List<TempoDescarregamentoPalete> findAll();

    @Override
    @EntityGraph(attributePaths = {"tipoVeiculo", "tipoCarga"})
    Optional<TempoDescarregamentoPalete> findById(Long id);

    boolean existsByTipoVeiculo_IdAndTipoCarga_Id(Long tipoVeiculoId, Long tipoCargaId);

    boolean existsByTipoVeiculo_IdAndTipoCarga_IdAndIdNot(Long tipoVeiculoId, Long tipoCargaId, Long id);
}
