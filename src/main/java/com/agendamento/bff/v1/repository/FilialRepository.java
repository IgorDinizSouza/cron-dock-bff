package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Filial;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FilialRepository extends JpaRepository<Filial, Long> {

    @EntityGraph(attributePaths = {"grupoEmpresarial"})
    List<Filial> findAllByGrupoEmpresarialId(Long grupoEmpresarialId);

    @EntityGraph(attributePaths = {"grupoEmpresarial"})
    Optional<Filial> findByIdAndGrupoEmpresarialId(Long id, Long grupoEmpresarialId);

    boolean existsByGrupoEmpresarialIdAndCnpjIgnoreCase(Long grupoEmpresarialId, String cnpj);

    boolean existsByGrupoEmpresarialIdAndCnpjIgnoreCaseAndIdNot(Long grupoEmpresarialId, String cnpj, Long id);
}
