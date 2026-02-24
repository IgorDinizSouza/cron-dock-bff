package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Regional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegionalRepository extends JpaRepository<Regional, Long> {

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);

    @Query("""
            select distinct r
            from Regional r
            left join fetch r.regionaisFiliais rf
            left join fetch rf.filial f
            left join fetch f.grupoEmpresarial
            """)
    List<Regional> findAllWithRegionaisFiliais();

    @Query("""
            select distinct r
            from Regional r
            left join fetch r.regionaisFiliais rf
            left join fetch rf.filial f
            left join fetch f.grupoEmpresarial
            where r.id = :id
            """)
    Optional<Regional> findByIdWithRegionaisFiliais(@Param("id") Long id);
}
