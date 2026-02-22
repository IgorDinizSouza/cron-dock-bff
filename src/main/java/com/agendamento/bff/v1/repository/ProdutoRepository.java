package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Produto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @EntityGraph(attributePaths = {"grupoEmpresarial", "comprador"})
    List<Produto> findAllByGrupoEmpresarialId(Long grupoEmpresarialId);

    @EntityGraph(attributePaths = {"grupoEmpresarial", "comprador"})
    Optional<Produto> findByIdAndGrupoEmpresarialId(Long id, Long grupoEmpresarialId);
}
