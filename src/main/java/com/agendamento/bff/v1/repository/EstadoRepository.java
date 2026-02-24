package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    boolean existsByUfIgnoreCase(String uf);

    Optional<Estado> findByDescricaoIgnoreCase(String descricao);
}
