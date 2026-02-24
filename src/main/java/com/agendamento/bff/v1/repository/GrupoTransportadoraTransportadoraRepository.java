package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.GrupoTransportadoraTransportadora;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoTransportadoraTransportadoraRepository extends JpaRepository<GrupoTransportadoraTransportadora, Long> {

    boolean existsByGrupoTransportadora_IdAndTransportadora_Id(Long grupoTransportadoraId, Long transportadoraId);
}
