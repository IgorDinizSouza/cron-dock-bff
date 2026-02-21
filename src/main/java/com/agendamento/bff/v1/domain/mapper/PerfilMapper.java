package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.PerfilRequest;
import com.agendamento.bff.v1.domain.dto.response.PerfilResponse;
import com.agendamento.bff.v1.domain.model.Perfil;

public class PerfilMapper {

    private PerfilMapper() {
    }

    public static Perfil toEntity(PerfilRequest req) {
        return Perfil.builder()
                .descricao(req.descricao())
                .build();
    }

    public static PerfilResponse toResponse(Perfil entity) {
        return new PerfilResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getDataCriacao(),
                entity.getDataAlteracao(),
                entity.getRoles().stream().map(RoleMapper::toResponse).toList()
        );
    }
}
