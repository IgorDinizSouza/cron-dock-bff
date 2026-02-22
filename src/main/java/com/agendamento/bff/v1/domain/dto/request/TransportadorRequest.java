package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record TransportadorRequest(
        String descricao,
        String cnpj,
        Long grupoEmpresarialId,
        Status status
) {
}
