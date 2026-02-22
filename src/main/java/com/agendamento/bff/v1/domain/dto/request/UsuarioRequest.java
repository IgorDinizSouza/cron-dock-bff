package com.agendamento.bff.v1.domain.dto.request;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.util.List;

public record UsuarioRequest(
        String descricao,
        String email,
        String senha,
        Long grupoEmpresarialId,
        Status status,
        List<Long> perfilIds
) {
}
