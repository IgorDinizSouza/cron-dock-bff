package com.agendamento.bff.v1.domain.dto.response;

public record TipoCargaResponse(
        Long id,
        String descricao,
        Integer minSku,
        Integer maxSku
) {
}
