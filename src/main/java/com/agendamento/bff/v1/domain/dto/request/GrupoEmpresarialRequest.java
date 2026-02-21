package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

public record GrupoEmpresarialRequest(
        String descricao,
        String cnpj,
        Status status ) {
}
