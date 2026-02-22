package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Fornecedor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    @EntityGraph(attributePaths = {"grupoEmpresarial"})
    List<Fornecedor> findAllByGrupoEmpresarialId(Long grupoEmpresarialId);

    @EntityGraph(attributePaths = {"grupoEmpresarial"})
    Optional<Fornecedor> findByIdAndGrupoEmpresarialId(Long id, Long grupoEmpresarialId);

    boolean existsByGrupoEmpresarialIdAndCnpjIgnoreCase(Long grupoEmpresarialId, String cnpj);

    boolean existsByGrupoEmpresarialIdAndCnpjIgnoreCaseAndIdNot(Long grupoEmpresarialId, String cnpj, Long id);
}
