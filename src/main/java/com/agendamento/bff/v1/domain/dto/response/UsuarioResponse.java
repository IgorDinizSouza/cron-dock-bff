package com.agendamento.bff.v1.domain.dto.response;

import com.agendamento.bff.v1.domain.enumeration.Status;

import java.time.LocalDateTime;
import java.util.List;

public record UsuarioResponse(
        Long id,
        String descricao,
        String email,
        Status status,
        LocalDateTime dataCriacao,
        LocalDateTime dataAlteracao,
        Long grupoEmpresarialId,
        String grupoEmpresarialDescricao,
        List<PerfilResponse> perfis
) {
}
