package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record GrupoFilialResponse(
        Long id,
        String descricao,
        Status status
) {
}
