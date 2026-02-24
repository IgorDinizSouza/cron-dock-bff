package com.agendamento.bff.v1.domain.dto.response;

import java.time.LocalDateTime;

public record GrupoTransportadoraTransportadoraDetalheResponse(
        Long id,
        LocalDateTime dataCriacao,
        TransportadorResponse transportadora
) {
}
