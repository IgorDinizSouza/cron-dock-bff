package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.MotivoOcorrenciaRequest;
import com.agendamento.bff.v1.domain.dto.response.MotivoOcorrenciaResponse;
import com.agendamento.bff.v1.domain.model.MotivoOcorrencia;

public class MotivoOcorrenciaMapper {

    private MotivoOcorrenciaMapper() {
    }

    public static MotivoOcorrencia toEntity(MotivoOcorrenciaRequest req) {
        return MotivoOcorrencia.builder()
                .descricao(req.descricao())
                .build();
    }

    public static MotivoOcorrenciaResponse toResponse(MotivoOcorrencia entity) {
        return new MotivoOcorrenciaResponse(entity.getId(), entity.getDescricao());
    }
}
