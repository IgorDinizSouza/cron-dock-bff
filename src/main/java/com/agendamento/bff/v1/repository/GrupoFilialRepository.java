package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.GrupoFilial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GrupoFilialRepository extends JpaRepository<GrupoFilial, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);

    @Query("""
            select distinct g
            from GrupoFilial g
            left join fetch g.filiais gf
            left join fetch gf.filial f
            left join fetch f.grupoEmpresarial
            """)
    List<GrupoFilial> findAllWithFiliais();

    @Query("""
            select distinct g
            from GrupoFilial g
            left join fetch g.filiais gf
            left join fetch gf.filial f
            left join fetch f.grupoEmpresarial
            where g.id = :id
            """)
    Optional<GrupoFilial> findByIdWithFiliais(@Param("id") Long id);
}
