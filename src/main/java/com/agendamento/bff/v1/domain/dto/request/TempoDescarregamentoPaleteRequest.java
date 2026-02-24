package com.agendamento.bff.v1.domain.dto.request;

public record TempoDescarregamentoPaleteRequest(
        Long tipoVeiculoId,
        Long tipoCargaId,
        Integer minuto
) {
}
