package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Comprador;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompradorRepository extends JpaRepository<Comprador, Long> {

    @EntityGraph(attributePaths = {"grupoEmpresarial"})
    List<Comprador> findAllByGrupoEmpresarialId(Long grupoEmpresarialId);

    @EntityGraph(attributePaths = {"grupoEmpresarial"})
    Optional<Comprador> findByIdAndGrupoEmpresarialId(Long id, Long grupoEmpresarialId);

    boolean existsByGrupoEmpresarialIdAndDescricaoIgnoreCase(Long grupoEmpresarialId, String descricao);

    boolean existsByGrupoEmpresarialIdAndDescricaoIgnoreCaseAndIdNot(Long grupoEmpresarialId, String descricao, Long id);
}
