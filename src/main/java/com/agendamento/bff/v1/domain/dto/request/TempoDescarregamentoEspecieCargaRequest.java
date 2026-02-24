package com.agendamento.bff.v1.domain.dto.request;

public record TempoDescarregamentoEspecieCargaRequest(
        Long especieCargaId,
        Integer minuto
) {
}
