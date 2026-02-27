package com.agendamento.bff.v1.repository;

import com.agendamento.bff.v1.domain.model.UsuarioAprovacao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioAprovacaoRepository extends JpaRepository<UsuarioAprovacao, Long> {

    @EntityGraph(attributePaths = {"usuarioSolicitante", "usuarioAprovador", "statusAprovacaoUsuario"})
    Optional<UsuarioAprovacao> findByUsuarioSolicitanteId(Long usuarioSolicitanteId);

    @EntityGraph(attributePaths = {
            "usuarioSolicitante",
            "usuarioSolicitante.grupoEmpresarial",
            "usuarioSolicitante.perfis",
            "usuarioSolicitante.perfis.roles",
            "statusAprovacaoUsuario"
    })
    List<UsuarioAprovacao> findAllByStatusAprovacaoUsuarioId(Long statusAprovacaoUsuarioId);

    @Query("""
            select ua.usuarioSolicitante.id
            from UsuarioAprovacao ua
            where ua.statusAprovacaoUsuario.id = :statusAprovacaoUsuarioId
              and ua.usuarioSolicitante.grupoEmpresarial.id = :grupoEmpresarialId
            """)
    List<Long> findIdsUsuarioSolicitanteByStatusAprovacaoUsuarioIdAndGrupoEmpresarialId(
            Long statusAprovacaoUsuarioId,
            Long grupoEmpresarialId
    );
}
