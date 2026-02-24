package com.agendamento.bff.v1.domain.dto.response;

public record TempoDescarregamentoPaleteResponse(
        Long id,
        Long tipoVeiculoId,
        Long tipoCargaId,
        Integer minuto
) {
}
