package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.MotivoNoShow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotivoNoShowRepository extends JpaRepository<MotivoNoShow, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);
}
