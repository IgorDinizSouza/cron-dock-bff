package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.GrupoFilialFilial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoFilialFilialRepository extends JpaRepository<GrupoFilialFilial, Long> {

    boolean existsByGrupoFilial_IdAndFilial_Id(Long grupoFilialId, Long filialId);

    Optional<GrupoFilialFilial> findByIdAndGrupoFilial_Id(Long id, Long grupoFilialId);
}
