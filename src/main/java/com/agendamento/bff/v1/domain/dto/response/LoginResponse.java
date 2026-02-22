package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.util.List;

public record LoginResponse(
        Long id,
        String descricao,
        String email,
        Status status,
        Long grupoEmpresarialId,
        String grupoEmpresarialDescricao,
        List<PerfilResponse> perfis
) {
}
