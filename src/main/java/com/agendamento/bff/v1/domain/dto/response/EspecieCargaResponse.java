package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record EspecieCargaResponse(
        Long id,
        String descricao,
        Boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao
) {
}
