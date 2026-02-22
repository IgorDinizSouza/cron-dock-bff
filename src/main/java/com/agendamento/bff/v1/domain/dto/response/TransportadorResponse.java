package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record TransportadorResponse(
        Long id,
        String descricao,
        String cnpj,
        Status status,
        Long grupoEmpresarialId
) {
}
