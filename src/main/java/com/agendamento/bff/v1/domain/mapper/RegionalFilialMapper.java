package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.response.RegionalFilialResponse;
import com.agendamento.bff.v1.domain.model.RegionalFilial;

public class RegionalFilialMapper {

    private RegionalFilialMapper() {
    }

    public static RegionalFilialResponse toResponse(RegionalFilial entity) {
        return new RegionalFilialResponse(
                entity.getId(),
                entity.getRegional() != null ? entity.getRegional().getId() : null,
                entity.getFilial() != null ? entity.getFilial().getId() : null,
                entity.getDataCriacao()
        );
    }
}
