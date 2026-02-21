package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.RoleRequest;
import com.agendamento.bff.v1.domain.dto.response.RoleResponse;
import com.agendamento.bff.v1.domain.model.Role;

public class RoleMapper {

    private RoleMapper() {
    }

    public static Role toEntity(RoleRequest req) {
        return Role.builder()
                .nome(req.nome())
                .descricao(req.descricao())
                .build();
    }

    public static RoleResponse toResponse(Role entity) {
        return new RoleResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getDataCriacao(),
                entity.getDataAlteracao()
        );
    }
}
