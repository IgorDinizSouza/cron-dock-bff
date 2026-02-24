package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record GrupoFilialRequest(
        String descricao,
        Status status
) {
}
