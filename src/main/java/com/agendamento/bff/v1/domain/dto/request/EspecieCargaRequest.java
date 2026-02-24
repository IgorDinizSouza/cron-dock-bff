package com.agendamento.bff.v1.domain.dto.request;

public record EspecieCargaRequest(
        String descricao,
        Boolean ativo
) {
}
