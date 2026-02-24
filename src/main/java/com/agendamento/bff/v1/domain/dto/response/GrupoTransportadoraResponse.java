package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record GrupoTransportadoraResponse(
        Long id,
        String descricao,
        Status status
) {
}
