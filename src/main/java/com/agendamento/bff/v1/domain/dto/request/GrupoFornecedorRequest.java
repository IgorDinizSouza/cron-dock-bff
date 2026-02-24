package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record GrupoFornecedorRequest(
        String descricao,
        Status status
) {
}
