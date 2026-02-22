package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.UsuarioRequest;
import com.agendamento.bff.v1.domain.dto.response.UsuarioResponse;
import com.agendamento.bff.v1.domain.model.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toEntity(UsuarioRequest req) {
        return Usuario.builder()
                .descricao(req.descricao())
                .email(req.email())
                .senha(req.senha())
                .status(req.status())
                .build();
    }

    public static UsuarioResponse toResponse(Usuario entity) {
        return new UsuarioResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getEmail(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getDataAlteracao(),
                entity.getGrupoEmpresarial() != null ? entity.getGrupoEmpresarial().getId() : null,
                entity.getGrupoEmpresarial() != null ? entity.getGrupoEmpresarial().getDescricao() : null,
                entity.getPerfis().stream().map(PerfilMapper::toResponse).toList()
        );
    }
}
