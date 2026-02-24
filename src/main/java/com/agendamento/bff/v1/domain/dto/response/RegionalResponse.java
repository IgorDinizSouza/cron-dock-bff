package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.time.LocalDateTime;

public record RegionalResponse(
        Long id,
        String descricao,
        Status status,
        LocalDateTime dataCriacao
) {
}
