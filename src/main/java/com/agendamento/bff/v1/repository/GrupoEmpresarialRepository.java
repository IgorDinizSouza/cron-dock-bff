package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.GrupoEmpresarial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoEmpresarialRepository extends JpaRepository<GrupoEmpresarial, Long> {
    Optional<GrupoEmpresarial> findById(Long id);
    Optional<GrupoEmpresarial> findByCnpj(String cnpj);
}