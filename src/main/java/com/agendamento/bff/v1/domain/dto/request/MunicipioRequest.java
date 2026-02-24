package com.agendamento.bff.v1.domain.dto.request;

public record MunicipioRequest(
        String descricao,
        String codigoIbge,
        Long estadoId
) {
}
