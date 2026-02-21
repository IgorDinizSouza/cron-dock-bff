package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = {"grupoEmpresarial", "perfis", "perfis.roles"})
    List<Usuario> findAllByGrupoEmpresarialId(Long grupoEmpresarialId);

    @EntityGraph(attributePaths = {"grupoEmpresarial", "perfis", "perfis.roles"})
    Optional<Usuario> findByIdAndGrupoEmpresarialId(Long id, Long grupoEmpresarialId);

    boolean existsByGrupoEmpresarialIdAndEmailIgnoreCase(Long grupoEmpresarialId, String email);

    boolean existsByGrupoEmpresarialIdAndEmailIgnoreCaseAndIdNot(Long grupoEmpresarialId, String email, Long id);
}
