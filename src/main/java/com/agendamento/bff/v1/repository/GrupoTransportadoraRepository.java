package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.GrupoTransportadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GrupoTransportadoraRepository extends JpaRepository<GrupoTransportadora, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);

    @Query("""
            select distinct g
            from GrupoTransportadora g
            left join fetch g.transportadoras gt
            left join fetch gt.transportadora t
            left join fetch t.grupoEmpresarial
            """)
    List<GrupoTransportadora> findAllWithTransportadoras();

    @Query("""
            select distinct g
            from GrupoTransportadora g
            left join fetch g.transportadoras gt
            left join fetch gt.transportadora t
            left join fetch t.grupoEmpresarial
            where g.id = :id
            """)
    Optional<GrupoTransportadora> findByIdWithTransportadoras(@Param("id") Long id);
}
