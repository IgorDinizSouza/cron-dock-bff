package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record RegionalFilialResponse(
        Long id,
        Long regionalId,
        Long filialId,
        LocalDateTime dataCriacao
) {
}
