package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.MotivoOcorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotivoOcorrenciaRepository extends JpaRepository<MotivoOcorrencia, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);
}
