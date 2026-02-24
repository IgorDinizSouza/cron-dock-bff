package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.MotivoPriorizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotivoPriorizacaoRepository extends JpaRepository<MotivoPriorizacao, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);
}
