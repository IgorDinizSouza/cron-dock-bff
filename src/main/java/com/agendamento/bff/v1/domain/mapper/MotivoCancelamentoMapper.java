package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.MotivoCancelamentoRequest;
import com.agendamento.bff.v1.domain.dto.response.MotivoCancelamentoResponse;
import com.agendamento.bff.v1.domain.model.MotivoCancelamento;

public class MotivoCancelamentoMapper {

    private MotivoCancelamentoMapper() {
    }

    public static MotivoCancelamento toEntity(MotivoCancelamentoRequest req) {
        return MotivoCancelamento.builder()
                .descricao(req.descricao())
                .build();
    }

    public static MotivoCancelamentoResponse toResponse(MotivoCancelamento entity) {
        return new MotivoCancelamentoResponse(entity.getId(), entity.getDescricao());
    }
}
