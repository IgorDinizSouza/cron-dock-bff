package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.EspecieCarga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecieCargaRepository extends JpaRepository<EspecieCarga, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);
}
