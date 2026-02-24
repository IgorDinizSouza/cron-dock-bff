package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.TempoDescarregamentoEspecieCargaRequest;
import com.agendamento.bff.v1.domain.dto.response.TempoDescarregamentoEspecieCargaResponse;
import com.agendamento.bff.v1.domain.model.TempoDescarregamentoEspecieCarga;

public class TempoDescarregamentoEspecieCargaMapper {

    private TempoDescarregamentoEspecieCargaMapper() {
    }

    public static TempoDescarregamentoEspecieCarga toEntity(TempoDescarregamentoEspecieCargaRequest req) {
        return TempoDescarregamentoEspecieCarga.builder()
                .minuto(req.minuto())
                .build();
    }

    public static TempoDescarregamentoEspecieCargaResponse toResponse(TempoDescarregamentoEspecieCarga entity) {
        return new TempoDescarregamentoEspecieCargaResponse(
                entity.getId(),
                entity.getEspecieCarga() != null ? entity.getEspecieCarga().getId() : null,
                entity.getMinuto()
        );
    }
}
