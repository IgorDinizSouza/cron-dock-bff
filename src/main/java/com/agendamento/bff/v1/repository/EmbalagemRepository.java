package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Embalagem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EmbalagemRepository extends JpaRepository<Embalagem, Long> {

    @EntityGraph(attributePaths = {"grupoEmpresarial", "produto"})
    List<Embalagem> findAllByGrupoEmpresarialIdAndProdutoId(Long grupoEmpresarialId, Long produtoId);

    @EntityGraph(attributePaths = {"grupoEmpresarial", "produto"})
    List<Embalagem> findAllByGrupoEmpresarialIdAndProdutoIdIn(Long grupoEmpresarialId, Collection<Long> produtoIds);
}
