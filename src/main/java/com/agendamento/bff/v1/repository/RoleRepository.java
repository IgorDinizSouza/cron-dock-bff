package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Override
    @EntityGraph(attributePaths = {"perfis"})
    List<Role> findAll();

    @Override
    @EntityGraph(attributePaths = {"perfis"})
    Optional<Role> findById(Long id);

    @EntityGraph(attributePaths = {"perfis"})
    @Query("select r from Role r where r.id = :id")
    Optional<Role> findWithPerfisById(Long id);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    @Query(value = """
            select r.*
            from role_sistema r
            inner join perfil_role pr on pr.role_id = r.id
            where pr.perfil_id = :perfilId
            """, nativeQuery = true)
    List<Role> buscarPorPerfilIdTabelaAtual(@Param("perfilId") Long perfilId);

    @Query(value = """
            select r.*
            from role_sistema r
            inner join perfil_roles pr on pr.roles_id = r.id
            where pr.perfil_id = :perfilId
            """, nativeQuery = true)
    List<Role> buscarPorPerfilIdTabelaLegada(@Param("perfilId") Long perfilId);
}
