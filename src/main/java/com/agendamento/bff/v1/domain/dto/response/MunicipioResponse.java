package com.agendamento.bff.v1.domain.dto.response;

public record MunicipioResponse(
        Long id,
        String descricao,
        String codigoIbge,
        EstadoResponse estado
) {
}
