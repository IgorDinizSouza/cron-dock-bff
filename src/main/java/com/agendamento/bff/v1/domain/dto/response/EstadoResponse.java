package com.agendamento.bff.v1.domain.dto.response;

public record EstadoResponse(
        Long id,
        String descricao,
        String uf
) {
}
