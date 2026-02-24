package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record GrupoFilialFilialDetalheResponse(
        Long id,
        LocalDateTime data,
        FilialResponse filial
) {
}
