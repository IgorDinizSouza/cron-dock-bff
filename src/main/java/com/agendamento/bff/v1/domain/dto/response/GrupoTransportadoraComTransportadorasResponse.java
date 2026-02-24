package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.util.List;

public record GrupoTransportadoraComTransportadorasResponse(
        Long id,
        String descricao,
        Status status,
        List<GrupoTransportadoraTransportadoraDetalheResponse> transportadoras
) {
}
