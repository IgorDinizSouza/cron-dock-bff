package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.TipoCarga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoCargaRepository extends JpaRepository<TipoCarga, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);
}
