package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.RegionalFilial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionalFilialRepository extends JpaRepository<RegionalFilial, Long> {

    boolean existsByRegional_IdAndFilial_Id(Long regionalId, Long filialId);
}
