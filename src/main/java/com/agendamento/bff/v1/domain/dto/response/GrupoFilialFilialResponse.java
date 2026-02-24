package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record GrupoFilialFilialResponse(
        Long id,
        Long grupoFilialId,
        Long filialId,
        LocalDateTime data
) {
}
