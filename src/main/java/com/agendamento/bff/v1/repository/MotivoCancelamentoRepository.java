package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.MotivoCancelamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotivoCancelamentoRepository extends JpaRepository<MotivoCancelamento, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);
}
