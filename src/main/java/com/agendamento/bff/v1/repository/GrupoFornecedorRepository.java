package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.GrupoFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GrupoFornecedorRepository extends JpaRepository<GrupoFornecedor, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);

    @Query("""
            select distinct g
            from GrupoFornecedor g
            left join fetch g.fornecedores gf
            left join fetch gf.fornecedor f
            left join fetch f.grupoEmpresarial
            """)
    List<GrupoFornecedor> findAllWithFornecedores();

    @Query("""
            select distinct g
            from GrupoFornecedor g
            left join fetch g.fornecedores gf
            left join fetch gf.fornecedor f
            left join fetch f.grupoEmpresarial
            where g.id = :id
            """)
    Optional<GrupoFornecedor> findByIdWithFornecedores(@Param("id") Long id);
}
