package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.response.EstadoResponse;
import com.agendamento.bff.v1.domain.model.Estado;

public class EstadoMapper {

    private EstadoMapper() {
    }

    public static EstadoResponse toResponse(Estado entity) {
        if (entity == null) {
            return null;
        }
        return new EstadoResponse(
                entity.getId(),
                entity.getDescricao(),
                entity.getUf()
        );
    }
}
