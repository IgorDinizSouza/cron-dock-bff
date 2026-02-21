package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    @Query("select distinct p from Perfil p left join fetch p.roles")
    List<Perfil> findAllWithRoles();

    @Query("select p from Perfil p left join fetch p.roles where p.id = :id")
    Optional<Perfil> findByIdWithRoles(@Param("id") Long id);

    @Query("select distinct p from Perfil p left join fetch p.usuarios left join fetch p.roles where p.id = :id")
    Optional<Perfil> findWithUsuariosById(@Param("id") Long id);

    boolean existsByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndIdNot(String descricao, Long id);
}
