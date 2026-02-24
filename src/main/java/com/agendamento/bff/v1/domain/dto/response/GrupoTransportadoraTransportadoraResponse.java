package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record GrupoTransportadoraTransportadoraResponse(
        Long id,
        Long grupoTransportadoraId,
        Long transportadoraId,
        LocalDateTime dataCriacao
) {
}
