package com.agendamento.bff.v1.domain.dto.request;

public record TipoCargaRequest(
        String descricao,
        Integer minSku,
        Integer maxSku
) {
}
