package com.agendamento.bff.v1.domain.mapper;

import com.agendamento.bff.v1.domain.dto.request.TempoDescarregamentoPaleteRequest;
import com.agendamento.bff.v1.domain.dto.response.TempoDescarregamentoPaleteResponse;
import com.agendamento.bff.v1.domain.model.TempoDescarregamentoPalete;

public class TempoDescarregamentoPaleteMapper {

    private TempoDescarregamentoPaleteMapper() {
    }

    public static TempoDescarregamentoPalete toEntity(TempoDescarregamentoPaleteRequest req) {
        return TempoDescarregamentoPalete.builder()
                .minuto(req.minuto())
                .build();
    }

    public static TempoDescarregamentoPaleteResponse toResponse(TempoDescarregamentoPalete entity) {
        return new TempoDescarregamentoPaleteResponse(
                entity.getId(),
                entity.getTipoVeiculo() != null ? entity.getTipoVeiculo().getId() : null,
                entity.getTipoCarga() != null ? entity.getTipoCarga().getId() : null,
                entity.getMinuto()
        );
    }
}
