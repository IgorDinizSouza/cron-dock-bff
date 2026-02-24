package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.TempoDescarregamentoEspecieCarga;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TempoDescarregamentoEspecieCargaRepository extends JpaRepository<TempoDescarregamentoEspecieCarga, Long> {

    @Override
    @EntityGraph(attributePaths = {"especieCarga"})
    List<TempoDescarregamentoEspecieCarga> findAll();

    @Override
    @EntityGraph(attributePaths = {"especieCarga"})
    Optional<TempoDescarregamentoEspecieCarga> findById(Long id);

    boolean existsByEspecieCarga_Id(Long especieCargaId);

    boolean existsByEspecieCarga_IdAndIdNot(Long especieCargaId, Long id);
}
