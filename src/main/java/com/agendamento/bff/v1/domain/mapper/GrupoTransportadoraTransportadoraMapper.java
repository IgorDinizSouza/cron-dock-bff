package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.response.GrupoTransportadoraTransportadoraResponse;
import com.agendamento.bff.v1.domain.model.GrupoTransportadoraTransportadora;

public class GrupoTransportadoraTransportadoraMapper {

    private GrupoTransportadoraTransportadoraMapper() {
    }

    public static GrupoTransportadoraTransportadoraResponse toResponse(GrupoTransportadoraTransportadora entity) {
        return new GrupoTransportadoraTransportadoraResponse(
                entity.getId(),
                entity.getGrupoTransportadora() != null ? entity.getGrupoTransportadora().getId() : null,
                entity.getTransportadora() != null ? entity.getTransportadora().getId() : null,
                entity.getDataCriacao()
        );
    }
}
