package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.response.GrupoFilialFilialResponse;
import com.agendamento.bff.v1.domain.model.GrupoFilialFilial;

public class GrupoFilialFilialMapper {

    private GrupoFilialFilialMapper() {
    }

    public static GrupoFilialFilialResponse toResponse(GrupoFilialFilial entity) {
        return new GrupoFilialFilialResponse(
                entity.getId(),
                entity.getGrupoFilial() != null ? entity.getGrupoFilial().getId() : null,
                entity.getFilial() != null ? entity.getFilial().getId() : null,
                entity.getData()
        );
    }
}
