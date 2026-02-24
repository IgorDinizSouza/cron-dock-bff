package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.MotivoPriorizacaoRequest;
import com.agendamento.bff.v1.domain.dto.response.MotivoPriorizacaoResponse;
import com.agendamento.bff.v1.domain.model.MotivoPriorizacao;

public class MotivoPriorizacaoMapper {

    private MotivoPriorizacaoMapper() {
    }

    public static MotivoPriorizacao toEntity(MotivoPriorizacaoRequest req) {
        return MotivoPriorizacao.builder()
                .descricao(req.descricao())
                .build();
    }

    public static MotivoPriorizacaoResponse toResponse(MotivoPriorizacao entity) {
        return new MotivoPriorizacaoResponse(entity.getId(), entity.getDescricao());
    }
}
