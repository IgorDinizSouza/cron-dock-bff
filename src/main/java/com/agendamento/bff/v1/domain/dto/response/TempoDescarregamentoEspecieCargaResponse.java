package com.agendamento.bff.v1.domain.dto.response;

public record TempoDescarregamentoEspecieCargaResponse(
        Long id,
        Long especieCargaId,
        Integer minuto
) {
}
