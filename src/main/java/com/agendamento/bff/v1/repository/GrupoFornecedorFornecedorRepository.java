package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.GrupoFornecedorFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoFornecedorFornecedorRepository extends JpaRepository<GrupoFornecedorFornecedor, Long> {

    boolean existsByGrupoFornecedor_IdAndFornecedor_Id(Long grupoFornecedorId, Long fornecedorId);
}
